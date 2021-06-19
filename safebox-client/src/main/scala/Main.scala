import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec

object Main extends App {

  val testFile = new File("/Users/kuriakosemathew/Documents/work/safebox/safebox-client/src/main/resources/staging-area/headshot.jpg")
  val esource = new Tesbouncy()

  try{
    val fileStream = new FileInputStream(testFile)
    val fileData : Array[Byte] = new Array[Byte](testFile.length().toInt)
    fileStream.read(fileData)

    val key: String = "12345678"
    val enc : Array[Byte] = esource.Encrypt(key, fileData)
    val fileByteArray : Array[Byte] = enc

    val fileOutFile = new FileOutputStream("/Users/kuriakosemathew/Documents/work/safebox/safebox-client/src/main/resources/staging-area/pom-enc.jpg")
    fileOutFile.write(fileByteArray)
    fileOutFile.close()
  }

  val testFile2 = new File("/Users/kuriakosemathew/Documents/work/safebox/safebox-client/src/main/resources/staging-area/pom-enc.jpg")
  val key2: String = "12345678"
  val fileStream2 = new FileInputStream(testFile2)
  val fileData2 : Array[Byte] = new Array[Byte](testFile2.length().toInt)
  fileStream2.read(fileData2)

  val dec : Array[Byte] = esource.Decrypt(key2, fileData2)
  val fileByteArray2 : Array[Byte] = dec

  val fileOutFile2 = new FileOutputStream("/Users/kuriakosemathew/Documents/work/safebox/safebox-client/src/main/resources/staging-area/" +
    "pom-dec.jpg")
  fileOutFile2.write(fileByteArray2)
  fileOutFile2.close()

}
