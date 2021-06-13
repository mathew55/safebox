package routes

import java.io.File
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart.FormData
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, ExceptionHandler, RequestContext, RouteResult}
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.{ByteString, Timeout}
import spray.json._

object UploadRouter extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val system = ActorSystem("UploadingFiles")
  implicit val materializer = ActorMaterializer()
  val newline = ByteString("\n")

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val jsonStreamingSupport = EntityStreamingSupport.json()
    .withFramingRenderer(Flow[ByteString].map(bs => bs ++ newline))

  def uploadRoute: RequestContext => Future[RouteResult] = (pathEndOrSingleSlash & get) {
    complete(
      HttpEntity(
        ContentTypes.`text/html(UTF-8)`,
        """
          |<html>
          |  <body>
          |    <form action="/upload" method="post" enctype="multipart/form-data">
          |      <input type="file" name="myFile">
          |      <button type="submit">Upload</button>
          |    </form>
          |  </body>
          |</html>
          """.stripMargin
      )
    )
  } ~ (path("upload") & extractLog) { log =>
    post {
      entity(as[Multipart.FormData]) { formdata =>
        val partsSource: Source[FormData.BodyPart, Any] = formdata.parts //Get The bodyPart from the file Data
        println("Debug 2")
        val partsFlow: Flow[FormData.BodyPart, Either[(Source[ByteString, Any], File), Throwable], NotUsed] = Flow[Multipart.FormData.BodyPart].map { fileData => {
          println("Debug 3")
          if (fileData.name == "myFile") {
            val filename = "/Users/kuriakosemathew/Documents/work/safebox/safebox-server/server-data-bucket/" + fileData.filename.getOrElse("tempFile_" + System.currentTimeMillis())
            val file = new File(filename)
            val fileContentsSource = fileData.entity.dataBytes
            Left(fileContentsSource, file)
          } else {
            Right(new RuntimeException("Unexpected File"))
          }
        }
        }
        val fileSink = Sink.fold[Future[IOResult], Either[(Source[ByteString, Any], File), Throwable]](Future(new IOResult(1L, Success(Done)))) {
          case (_, Left((src, file))) =>
            src.runWith(FileIO.toPath(file.toPath))

          case (_, Right(ex)) =>
            Future(new IOResult(1L, Failure(ex)))

        }

        onComplete(partsSource.via(partsFlow).runWith(fileSink).flatten) {
          case Success(value) =>
            value match {
              case res@IOResult(_, _) =>
                if (res.wasSuccessful) complete("Successful Upload")
                else {
                  log.error(s"Exception ${res.getError}")
                  complete("Unsuccessful Upload")
                }
            }
          case Failure(ex) =>
            log.error(s"Exception Received $ex")
            complete("Unsuccessful Upload")
        }
      }
    }
  }
}