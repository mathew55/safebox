import org.bouncycastle.crypto._
import org.bouncycastle.crypto.engines._
import org.bouncycastle.crypto.modes._
import org.bouncycastle.crypto.paddings._
import org.bouncycastle.crypto.params._

/**
 * BouncyCastle Utilities implementation for decryption services.
 * Uses DESEngine for all crypto services.
 */
class EncryptionService {
  val engine = new DESEngine

  /**
   * BouncyCastle Encryption service utility
   * @param keys - key to encrypt the file
   * @param plainData - The data to be encrypted
   * @return - EncryptedData
   */
  def Encrypt(keys: String, plainData: Array[Byte]): Array[Byte] = {
    val key = keys.getBytes
    val ptBytes = plainData
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

  /**
   * BouncyCastle Decryption service utility
   * @param key2 - Key to decrypt the file
   * @param cipherText - The data to be decrypted
   * @return - DecryptedData
   */
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