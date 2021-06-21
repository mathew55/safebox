import scala.util.Try

trait Config {

  // config object

  // keys config
  lazy val keysDir = ".keys"
  lazy val publicKeyLocation = ".keys/id_rsa.pub"
  lazy val privateKeyLocation = ".keys/id_rsa"
  lazy val keySize = 409600
  lazy val serverUploadEndpoint = "http://localhost:5000/upload"
  lazy val uploadFilePath = "/Users/kuriakosemathew/Documents/work/safebox/safebox-client/src/main/resources/pom.xml"
  lazy val encryptionKey = "12345678"

}
