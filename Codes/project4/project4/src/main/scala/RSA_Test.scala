
import java.security._
import java.security.spec.X509EncodedKeySpec
import java.util
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64
import sun.misc.{BASE64Decoder, BASE64Encoder}

object RSA_Test extends App {

  def encryptRSAPublic(text:String,key:PublicKey):String = {
    var cipher: Cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.ENCRYPT_MODE, key);
    (Base64.encodeBase64String(cipher.doFinal(text.getBytes("UTF-8"))))
  }
  def encryptRSAPrivate(text:String,key:PrivateKey):String = {
    var cipher: Cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    (Base64.encodeBase64String(cipher.doFinal(text.getBytes("UTF-8"))))
  }

  def decryptRSAPrivate(text :String, key:PrivateKey):String  ={
    var cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, key)
    new String(cipher.doFinal(Base64.decodeBase64(text)),"UTF-8")
  }
  def decryptRSAPublic(text :String, key:PublicKey):String  ={
    var cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, key)
    new String(cipher.doFinal(Base64.decodeBase64(text)),"UTF-8")
  }

  def generateKey: KeyPair = {

    var keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024);
    var keypair = keyGen.generateKeyPair()
    keypair
  }
  var KeyPair = generateKey
  var privateKey: PrivateKey = KeyPair.getPrivate()
  var publicKey: PublicKey = KeyPair.getPublic()

  def encodePubKey(key:PublicKey):String = {
    var arr: Array[Byte] = key.getEncoded
    var encoder: BASE64Encoder = new BASE64Encoder()
    var pubKeytemp: String = encoder.encode(arr);
    pubKeytemp
  }

  def decodePubKey(enc: String): PublicKey = {
    var decoder : BASE64Decoder = new BASE64Decoder()
    var arr2: Array[Byte]= decoder.decodeBuffer(enc)
    var x509KeySpec: X509EncodedKeySpec = new X509EncodedKeySpec(arr2)
    var keyFact:KeyFactory = KeyFactory.getInstance("RSA");
    var pubKey2 = keyFact.generatePublic(x509KeySpec)
    pubKey2
  }

  def toHash(data: String): String = {
    val sha = MessageDigest.getInstance("SHA-256")
    sha.update(data.getBytes("UTF-8"))
    val digest = sha.digest();
    val hexString = new StringBuffer();
    for (j <- 0 to digest.length - 1) {
      val hex = Integer.toHexString(0xff & digest(j));
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    val str = hexString.toString
    return str
  }

  println(publicKey)
  println(encodePubKey(publicKey))
  println(decodePubKey(encodePubKey(publicKey)))


  /*

    var text = "Pramit Dutta"
    val EncryptStr = Encryption.encryptRSAPrivate(text,privateKey)
    print(EncryptStr)
    println()

    val DecryptStr = Encryption.decryptRSAPublic(EncryptStr,publicKey)
    println(DecryptStr)
    */
}