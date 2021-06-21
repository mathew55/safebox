import Client.uploadFilePath

import java.io.{File, FileInputStream, FileOutputStream}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Source}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{Future, Promise}

object Client extends App with Config {

  val testConf = ConfigFactory.parseString(
    """
    akka.loglevel = INFO
    akka.log-dead-letters = off""")

  val encryptionService = new EncryptionService()
  implicit val system = ActorSystem("UploadingFiles", testConf)
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val testFile = new File(uploadFilePath)


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

  val uri = Uri(serverUploadEndpoint)
  val fileStream = new FileInputStream(testFile)
  val fileName = uploadFilePath.split("/").last
  val fileData: Array[Byte] = new Array[Byte](testFile.length().toInt)
  fileStream.read(fileData)

  val encryptedFile = encryptionService.Encrypt(encryptionKey , fileData)
  val reqEntity = Array[Byte]()

  val encryptedFileByteArray : Array[Byte] = fileData
  val encryptedFilePath = s"src/main/resources/staging-area/${fileName}-enc"
  val encryptedFileOutFile = new FileOutputStream(encryptedFilePath)
  encryptedFileOutFile.write(encryptedFileByteArray)
  encryptedFileOutFile.close()
  val fileToUpload = new File(encryptedFilePath)
  val result =
    for {

      req ← createRequest(uri, fileToUpload)
      _ = println(s"Running request, uploading test file of size ${testFile.length} bytes")
      response ← Http().singleRequest(req)
      responseBodyAsString ← Unmarshal(response).to[String]
    } yield responseBodyAsString

  result.onComplete { res ⇒
    println(s"The result was $res")
  }


}