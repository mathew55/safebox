import scala.util.Try

trait Config {

  // config object

  // keys config
  lazy val keysDir = ".keys"
  lazy val publicKeyLocation = ".keys/id_rsa.pub"
  lazy val privateKeyLocation = ".keys/id_rsa"
  lazy val keySize = 409600

}
