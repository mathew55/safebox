package com.safebox.routes

import java.io.File
import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.{Done, NotUsed}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart.FormData
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ RequestContext, RouteResult}
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}
import akka.stream.IOResult
import akka.util.ByteString
import com.safebox.AkkaSysProperties
import spray.json._


class UploadRouter extends DefaultJsonProtocol with SprayJsonSupport with AkkaSysProperties{

  import scala.concurrent.ExecutionContext.Implicits.global

  def uploadRoute: RequestContext => Future[RouteResult] = (pathEndOrSingleSlash & get) {
    complete(
      HttpEntity(
        ContentTypes.`text/html(UTF-8)`,
        """
          |<html>
          |  <body>
          |  </br></br><h1>Welcome To SafeBox</h1>
          |  <h2>This Page is for uploading without Encryption, Check the Readme for Uploading with Encryption</h2>
          |    <form action="/upload" method="post" enctype="multipart/form-data">
          |      <input type="file" name="UploadWithoutEncryption">
          |      <button type="submit">Upload</button>
          |    </form>
          |  </body>
          |</html>
          """.stripMargin
      )
    )
  } ~ (path("upload") & extractLog) { log =>
    post {
      log.info("Request recieved for uploading file")
      try{
        entity(as[Multipart.FormData])
      }catch {
        case e: Exception => log.info("Had an IOException trying to read that file")
      }
      entity(as[Multipart.FormData]) { formdata =>
        val partsSource: Source[FormData.BodyPart, Any] = formdata.parts //Get The bodyPart from the file Data
        val partsFlow: Flow[FormData.BodyPart, Either[(Source[ByteString, Any], File), Throwable], NotUsed] = Flow[Multipart.FormData.BodyPart].map { fileData => {
          if (fileData.name == "UploadWithoutEncryption") {
            log.info("Uploading Unencrypted file for UI")
            val filename = "src/main/resources/" + fileData.filename.getOrElse("tempFile_" + System.currentTimeMillis())
            val file = new File(filename)
            val fileContentsSource = fileData.entity.dataBytes
            Left(fileContentsSource, file)
          } else if (fileData.name != "UploadWithoutEncryption") {
            log.info("Uploading encrypted file")
            val filename = "src/main/resources/" + fileData.filename.getOrElse("tempFile_" + System.currentTimeMillis())
            val file = new File(filename)
            val fileContentsSource = fileData.entity.dataBytes
            Left(fileContentsSource, file)
          }else {
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
                if (res.wasSuccessful) complete("File Successfully Uploaded")
                else {
                  log.error(s"Exception ${res.getError}")
                  complete("File Upload was unsuccessfull")
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
object UploadRouter{
  def apply: List[RequestContext => Future[RouteResult]] ={
    val uploadRouterObject = new UploadRouter()
    List(uploadRouterObject.uploadRoute)
  }
}