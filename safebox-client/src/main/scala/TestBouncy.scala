import org.bouncycastle.crypto._
import org.bouncycastle.crypto.engines._
import org.bouncycastle.crypto.modes._
import org.bouncycastle.crypto.paddings._
import org.bouncycastle.crypto.params._


class Tesbouncy {
  val engine = new DESEngine

  def Encrypt(keys: String, plainText: Array[Byte]): Array[Byte] = {
    val key = keys.getBytes
    val ptBytes = plainText
    val cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine))
    cipher.init(true, new KeyParameter(key))
    val rv = new Array[Byte](cipher.getOutputSize(ptBytes.length))
    val tam = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0)
    try cipher.doFinal(rv, tam)
    catch {
      case ce: Exception =>
        ce.printStackTrace()
    }
    rv
  }

  def Decrypt(key2: String, cipherText: Array[Byte]): Array[Byte] = {
    val key = key2.getBytes
    val cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine))
    cipher.init(false, new KeyParameter(key))
    val rv = new Array[Byte](cipher.getOutputSize(cipherText.length))
    val tam = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0)
    try cipher.doFinal(rv, tam)
    catch {
      case ce: Exception =>
        ce.printStackTrace()
    }
    rv
  }
}