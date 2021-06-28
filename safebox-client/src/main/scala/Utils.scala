import Client.{encryptionKey, encryptionService, serverUploadEndpoint}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes, Multipart, RequestEntity, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Source}

import java.io.{File, FileInputStream, FileOutputStream}
import scala.concurrent.Future

object Utils {

  import Client.system
  import Client.system.dispatcher
  implicit val materializer = ActorMaterializer()

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


  def uploadFile(filepath: String): Unit = {
    val fileToUpload = new File(filepath)
    val uri = Uri(serverUploadEndpoint)
    val stagedFile = encryptAndStageFile(fileToUpload)

    val result =
      for {
        req ← createRequest(uri, stagedFile)
        _ = println(s"Running request, uploading test file of size ${stagedFile.length} bytes")
        response ← Http().singleRequest(req)
        responseBodyAsString ← Unmarshal(response).to[String]
      } yield responseBodyAsString

    result.onComplete { res ⇒
      println(s"The result was $res")
    }
  }

  def downloadFile(filepath: String): Unit = {
    val fileToUpload = new File(filepath)
    val uri = Uri(serverUploadEndpoint)
    val stagedFile = encryptAndStageFile(fileToUpload)

    val result =
      for {
        req ← createRequest(uri, stagedFile)
        _ = println(s"Running request, uploading test file of size ${stagedFile.length} bytes")
        response ← Http().singleRequest(req)
        responseBodyAsString ← Unmarshal(response).to[String]
      } yield responseBodyAsString

    result.onComplete { res ⇒
      println(s"The result was $res")
    }
  }

  def encryptAndStageFile(fileToUpload: File) = {
    val fileName = fileToUpload.getName
    print(s"Filename - ${fileName}")
    val fileStream = new FileInputStream(fileToUpload)
    val fileData: Array[Byte] = new Array[Byte](fileToUpload.length().toInt)
    fileStream.read(fileData)
    val encryptedFile: Array[Byte] = encryptionService.Encrypt(encryptionKey, fileData)
    val encryptedFileByteArray: Array[Byte] = encryptedFile

    val encryptedFilePath = s"src/main/resources/upload-staging-area/${fileName}-enc"
    val encryptedFileOut = new File(encryptedFilePath)
    val encryptedFileOutFile = new FileOutputStream(encryptedFileOut)
    encryptedFileOutFile.write(encryptedFileByteArray)
    encryptedFileOutFile.close()

    new File(encryptedFilePath)
  }
}
