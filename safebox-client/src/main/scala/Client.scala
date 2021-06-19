import java.io.{File, FileInputStream}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import FileUpload._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.Uri._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Source}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{Future, Promise}

object Client extends App {
  val testConf = ConfigFactory.parseString(
    """
    akka.loglevel = INFO
    akka.log-dead-letters = off""")
  implicit val system = ActorSystem("UploadingFiles", testConf)
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val testFile = new File("/Users/kuriakosemathew/Documents/work/safebox/safebox-client/src/main/resources/pom.xml")


  def createEntity(file: File): Future[RequestEntity] = {
    require(file.exists())
    val formData =
      Multipart.FormData(
        Source.single(
          Multipart.FormData.BodyPart(
            "test",
            HttpEntity(MediaTypes.`multipart/form-data`, file.length(), FileIO.fromFile(file, chunkSize = 100000)), // the chunk size here is currently critical for performance
            Map("filename" -> file.getName))))
    Marshal(formData).to[RequestEntity]
  }


  def createRequest(target: Uri, file: File): Future[HttpRequest] =
    for {
      e ← createEntity(file)
    } yield HttpRequest(HttpMethods.POST, uri = target, entity = e)

  val uri = Uri("http://localhost:5000/upload")
  val fileStream = new FileInputStream(testFile)
  val fileData : Array[Byte] = new Array[Byte](testFile.length().toInt)
  fileStream.read(fileData)
  println(fileData)
  val reqEntity = Array[Byte]()

  val result =
    for {
      req ← createRequest(uri, testFile)
      _ = println(s"Running request, uploading test file of size ${testFile.length} bytes")
      response ← Http().singleRequest(req)
      responseBodyAsString ← Unmarshal(response).to[String]
    } yield responseBodyAsString

//  try {
//    val respEntity = for {
//      request <- Marshal(fileData).to[RequestEntity]
//      response <- Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = uri, entity = request))
//      entity <- Unmarshal(response.entity).to[ByteString]
//    } yield entity

    result.onComplete { res ⇒
      println(s"The result was $res")
    }

//    system.scheduler.scheduleOnce(60.seconds) {
//      println("Shutting down after timeout...")
//      system.terminate()
//    }
//  } catch {
//    case _: Throwable ⇒ system.terminate()
//  }

}