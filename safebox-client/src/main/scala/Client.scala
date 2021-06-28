import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import Utils.{ downloadFile, uploadFile}

object Client extends App with Config {

  val clientConfiguration = ConfigFactory.parseString(
    """
    akka.loglevel = INFO
    akka.log-dead-letters = off""")

  val encryptionService = new EncryptionService()
  implicit val system = ActorSystem("UploadingFiles", clientConfiguration)

  if (args.length != 1) {
    println("Please retry by passing correct configuration in format - {upload/download} {filename/filedirectory}")
  }

  val service = args(0)
  val fileLocation = args(1)

  println(s"Requested Service - ${service}")
  println(s"File/Directory Location - ${fileLocation}")

  if (service.toLowerCase() == "upload") uploadFile(fileLocation)
  else downloadFile(fileLocation)

}