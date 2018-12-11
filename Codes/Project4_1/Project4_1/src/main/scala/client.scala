/**
  * Created by Hp on 11/27/2015.
  */

import java.security.{PublicKey, PrivateKey, MessageDigest}
import java.util
import javax.crypto.{SecretKey, Cipher}
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64
import java.security._
import java.security.spec.X509EncodedKeySpec
import javax.crypto._
import org.apache.commons.codec.binary.Base64
import spray.http._
import spray.client.pipelining._
import akka.actor.{Props, ActorSystem}
import sun.misc.BASE64Encoder
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


object ClientComplete extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

    /*************************************************************************************************************
     The Encryption and Description Function (Source : https://gist.github.com/alexandru/ac1c01168710786b54b0)
    *************************************************************************************************************/

  var numOfUsers:Int = 100
  var m = 100
  var p = 100
  var f = 100

  var userPrKey = new java.util.HashMap[String, PrivateKey]()
  var userPuKey = new java.util.HashMap[String, PublicKey]()
  var userToken = new java.util.HashMap[String, String]()
  var msgId_ASEkey = new java.util.HashMap[String, String]()
  var pmsgId_ASEkey = new java.util.HashMap[String, String]()
  var photoId_ASEkey = new java.util.HashMap[String, String]()
  var idem = new java.util.HashMap[String, String]()
  var idepm = new java.util.HashMap[String, String]()
  var idep = new java.util.HashMap[String, String]()
  var tempstr1:String = Random.alphanumeric.take(8).mkString
  var token:String = " "

  var KeyPair = RSA_Test.generateKey
  var serverPuKey:PublicKey = KeyPair.getPublic()  // public key of server

  var KeyPair1 = RSA_Test.generateKey
  var privateKey1: PrivateKey = KeyPair1.getPrivate()
  var publicKey1: PublicKey = KeyPair1.getPublic()
  var tokenReceived:String = RSA_Test.encryptRSAPublic(tempstr1,publicKey1)

  val pipeline = sendReceive

  val securePipeline = addCredentials(BasicHttpCredentials("adam", "1234")) ~> sendReceive
  println(s"Initing process ")
  //pipeline(Post("http://localhost:8080/myFacebook/addUser?id=user3&fname=Tom&lname=Haykin&birth=01-01-2015&locationFrom=UK&locationIn=US"))


  // generating users randomly
  for (a <- 0 to numOfUsers) {
    // generate Id
    var tempId: String = "user"+a

    // generate RSA pair
    var KeyPair = RSA_Test.generateKey
    var privateKey: PrivateKey = KeyPair.getPrivate()
    var publicKey: PublicKey = KeyPair.getPublic()

    // input pub and private key
    userPrKey.put(tempId, privateKey)
    userPuKey.put(tempId, publicKey)


    // generate token
    val temptoken: String = "user"+a+"password"

    // input token
    userToken.put(tempId, temptoken)
    //println(userToken.get(tempId)) // test

    pipeline(Post("http://localhost:8080/myFacebook/addUser?id="+tempId+"&fname="+Random.alphanumeric.take(8).mkString+"&lname="+Random.alphanumeric.take(8).mkString+"&birth="+Random.nextInt(11)+1+"-"+Random.nextInt(29)+1+"-"+Random.nextInt(2015)+"&locationFrom="+Random.alphanumeric.take(2).mkString+"&locationIn="+Random.alphanumeric.take(2).mkString+"&token="+temptoken))
    Thread.sleep(100L)

    //pipeline(Post("http://localhost:8080/myFacebook/addUser?id=user"+a+"&fname="+Random.alphanumeric.take(8).mkString+"&lname="+Random.alphanumeric.take(8).mkString+"&birth="+Random.nextInt(11)+1+"-"+Random.nextInt(29)+1+"-"+Random.nextInt(2015)+"&locationFrom="+Random.alphanumeric.take(2).mkString+"&locationIn="+Random.alphanumeric.take(2).mkString+"&token="+Random.alphanumeric.take(2).mkString))

  }




  // Authentication testing (3 steps)
  // 1)
  var tempId: String = "user10"

  // change public key to string
  //println(userPuKey.get(tempId))
  //println()
  //var xc:PublicKey = userPuKey.get(tempId)
  //println(xc.modulus)
  //println()
  //var arr: Array[Byte]=userPuKey.get(tempId).getEncoded
  //var encoder : BASE64Encoder = new BASE64Encoder()
  //var str: String = encoder.encode(arr);
  var userPubKey: PublicKey = userPuKey.get(tempId)
  var tempUserkey: String = RSA_Test.encodePubKey(publicKey1)
  var tempstr:String = tempUserkey.replaceAll("[\r\n]+","")
  tempstr1 = tempstr.replaceAll("==","")

  //println(tempUserkey)
  //println()
  //println(tempstr1)
  //var tempPubkey: PublicKey = RSA_Test.decodePubKey(tempstr1)
  //println()
  //println(tempPubkey)

  //println(tempkey)
  //var substr:String = tempkey.substring(1,tempkey.length)
  //tempkey = tempkey.substring(1,tempkey.length)
  //println(tempUserkey)
  //println()
  //println()
  var x1: String = "http://localhost:8080/myFacebookAu/publicKeySent?sentpubKeyUser="+tempstr1+"&sentuserId="+tempId
  //println(x1)
  pipeline(Post(x1))
  Thread.sleep(100L)

  // 2)
  var result1Au = securePipeline(Get("http://localhost:8080/myFacebookAu/getToken"))
  Thread.sleep(300L)
  // decrypt with private key of client
  //var tempstr12: String = result1Au.toString
  //println(tempstr12)
  //var tokenEncrypted1:String = RSA_Test.decryptRSAPrivate(tempstr12,userPrKey.get(tempId))

  //println(tokenEncrypted1)
  var tempstr12: String = " "
  var prtemp : PrivateKey = privateKey1//userPrKey.get(tempId)
  result1Au.foreach { response =>
    var tokenreceived:String = response.entity.asString
    tempstr12 = response.entity.asString
    token = RSA_Test.decryptRSAPrivate(tokenReceived,privateKey1)
    //println(tempstr12)
    //println(tempstr12.length)
    //tempstr12 = tempstr12.substring(0,tempstr12.length-1)
    //println(tempstr12)
    //var prtemp : PrivateKey = userPrKey.get(tempId)
    //println(prtemp)
    //vd1:String = RSA_Test.decryptRSAPrivate(tempstr12,prtemp)
    //println(tar tokenEncrypteokenEncrypted1)
    //println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
  }
  //var vd1:String = RSA_Test.decryptRSAPrivate(tempstr12,prtemp)

  // 3)

  //var tempname1:Array[Byte] = tempstr1.getBytes()
  //println(tempname1)
  //println(userPrKey.get(tempId))
  //var keyname:PrivateKey = userPrKey.get(tempId)

  var tempstr2:String = RSA_Test.encryptRSAPrivate(token,privateKey1)
  tempstr2 = tempstr2.substring(0,tempstr2.length-1)
  //var tempstr2: String = RSA_Test.decryptRSAPrivate(tempstr1.getBytes(),userPrKey.get(tempId))
  //println(tempstr2)

  pipeline(Post("http://localhost:8080/myFacebookAu/ETokenSent?sentEToken="+tempstr2))
  //Thread.sleep(100L)





      // generating messages randomly
      for (b <- 0 to numOfUsers/4) {
        for (a <- 0 to m*80/100) {
          // generate msgId
          var msgId: String = "mess"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

          // generate uId
          var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
          //println(uId)

          // generate msg
          var msg: String = Random.alphanumeric.take(16).mkString
          //println(msg)

          // generate ASE key
          var msgIdASE:String = msgId+uId+msg.substring(3,6)
          //println(msgIdASE)

          // input msgId and ASE key
          msgId_ASEkey.put(msgId, msgIdASE)
          //println(msgId_ASEkey.get(msgId))

          // encrypt msg
          var tempmsgE :String = " "
          var tempstr1a:String = AES_Test.encrypt(msgIdASE,msg)
          //println(tempstr1a)
          idem.put(msgId, tempstr1a)
          //println(AES_Test.decrypt(msgId_ASEkey.get(msgId),idem.get(msgId)))
          if (tempstr1a.length == 44) {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-1)
            tempmsgE = tempstr1a
          }
          else {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-2)
            tempmsgE = tempstr1a
          }



          //println(tempmsgE)
          //tempmsgE.replace('=', 'a')
          //var temp:String = tempmsgE.substring(0,tempmsgE.length-2)
          //println(temp)
          //println(tempmsgE.indexOf("="))
          //val tempmsgE :String = "aaaa/aaa"
          //println(tempmsgE)
          //tempmsgE.replace('/', 'a')
          //println(tempmsgE)

          pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId="+msgId+"&userId="+uId+"&messageFeed="+tempmsgE+"&location="+Random.alphanumeric.take(2).mkString))
          Thread.sleep(20L)

          //pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId=mess"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1+"&messageFeed="+Random.alphanumeric.take(8).mkString+"&location="+Random.alphanumeric.take(2).mkString))
        }
      }




      for (b <- (numOfUsers/4)+1 to 2*numOfUsers/4) {
        for (a <- 0 to m*60/100) {
          // generate msgId
          var msgId: String = "mess"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

          // generate uId
          var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
          //println(uId)

          // generate msg
          var msg: String = Random.alphanumeric.take(16).mkString
          //println(msg)

          // generate ASE key
          var msgIdASE:String = msgId+uId+msg.substring(3,6)
          //println(msgIdASE)

          // input msgId and ASE key
          msgId_ASEkey.put(msgId, msgIdASE)
          //println(msgId_ASEkey.get(msgId))

          // encrypt msg
          var tempmsgE :String = " "
          var tempstr1a:String = AES_Test.encrypt(msgIdASE,msg)
          //println(tempstr1a)
          idem.put(msgId, tempstr1a)
          //println(AES_Test.decrypt(msgId_ASEkey.get(msgId),idem.get(msgId)))
          if (tempstr1a.length == 44) {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-1)
            tempmsgE = tempstr1a
          }
          else {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-2)
            tempmsgE = tempstr1a
          }



          //println(tempmsgE)
          //tempmsgE.replace('=', 'a')
          //var temp:String = tempmsgE.substring(0,tempmsgE.length-2)
          //println(temp)
          //println(tempmsgE.indexOf("="))
          //val tempmsgE :String = "aaaa/aaa"
          //println(tempmsgE)
          //tempmsgE.replace('/', 'a')
          //println(tempmsgE)

          pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId="+msgId+"&userId="+uId+"&messageFeed="+tempmsgE+"&location="+Random.alphanumeric.take(2).mkString))
          Thread.sleep(20L)

          //pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId=mess"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1+"&messageFeed="+Random.alphanumeric.take(8).mkString+"&location="+Random.alphanumeric.take(2).mkString))
        }
      }
      for (b <- (2*numOfUsers/4)+1 to 3*numOfUsers/4) {
        for (a <- 0 to m*40/100) {
          // generate msgId
          var msgId: String = "mess"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

          // generate uId
          var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
          //println(uId)

          // generate msg
          var msg: String = Random.alphanumeric.take(16).mkString
          //println(msg)

          // generate ASE key
          var msgIdASE:String = msgId+uId+msg.substring(3,6)
          //println(msgIdASE)

          // input msgId and ASE key
          msgId_ASEkey.put(msgId, msgIdASE)
          //println(msgId_ASEkey.get(msgId))

          // encrypt msg
          var tempmsgE :String = " "
          var tempstr1a:String = AES_Test.encrypt(msgIdASE,msg)
          //println(tempstr1a)
          idem.put(msgId, tempstr1a)
          //println(AES_Test.decrypt(msgId_ASEkey.get(msgId),idem.get(msgId)))
          if (tempstr1a.length == 44) {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-1)
            tempmsgE = tempstr1a
          }
          else {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-2)
            tempmsgE = tempstr1a
          }



          //println(tempmsgE)
          //tempmsgE.replace('=', 'a')
          //var temp:String = tempmsgE.substring(0,tempmsgE.length-2)
          //println(temp)
          //println(tempmsgE.indexOf("="))
          //val tempmsgE :String = "aaaa/aaa"
          //println(tempmsgE)
          //tempmsgE.replace('/', 'a')
          //println(tempmsgE)

          pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId="+msgId+"&userId="+uId+"&messageFeed="+tempmsgE+"&location="+Random.alphanumeric.take(2).mkString))
          Thread.sleep(20L)

          //pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId=mess"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1+"&messageFeed="+Random.alphanumeric.take(8).mkString+"&location="+Random.alphanumeric.take(2).mkString))
        }
      }
      for (b <- (3*numOfUsers/4)+1 to 4*numOfUsers/4) {
        for (a <- 0 to m*20/100) {
          // generate msgId
          var msgId: String = "mess"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

          // generate uId
          var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
          //println(uId)

          // generate msg
          var msg: String = Random.alphanumeric.take(16).mkString
          //println(msg)

          // generate ASE key
          var msgIdASE:String = msgId+uId+msg.substring(3,6)
          //println(msgIdASE)

          // input msgId and ASE key
          msgId_ASEkey.put(msgId, msgIdASE)
          //println(msgId_ASEkey.get(msgId))

          // encrypt msg
          var tempmsgE :String = " "
          var tempstr1a:String = AES_Test.encrypt(msgIdASE,msg)
          //println(tempstr1a)
          idem.put(msgId, tempstr1a)
          //println(AES_Test.decrypt(msgId_ASEkey.get(msgId),idem.get(msgId)))
          if (tempstr1a.length == 44) {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-1)
            tempmsgE = tempstr1a
          }
          else {
            tempstr1a = tempstr1a.substring(0,tempstr1a.length-2)
            tempmsgE = tempstr1a
          }



          //println(tempmsgE)
          //tempmsgE.replace('=', 'a')
          //var temp:String = tempmsgE.substring(0,tempmsgE.length-2)
          //println(temp)
          //println(tempmsgE.indexOf("="))
          //val tempmsgE :String = "aaaa/aaa"
          //println(tempmsgE)
          //tempmsgE.replace('/', 'a')
          //println(tempmsgE)

          pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId="+msgId+"&userId="+uId+"&messageFeed="+tempmsgE+"&location="+Random.alphanumeric.take(2).mkString))
          Thread.sleep(20L)

          //pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId=mess"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1+"&messageFeed="+Random.alphanumeric.take(8).mkString+"&location="+Random.alphanumeric.take(2).mkString))
        }
      }



  // generating private messages randomly
  for (b <- 0 to numOfUsers/4) {
    for (a <- 0 to m*80/100) {
      // generate msgId
      var msgId: String = "mess"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

      // generate uId
      var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
      //println(uId)

      // generate msg
      var msg: String = Random.alphanumeric.take(16).mkString
      //println(msg)

      // generate ASE key
      var msgIdASE:String = msgId+uId+msg.substring(3,6)
      //println(msgIdASE)

      // input msgId and ASE key
      pmsgId_ASEkey.put(msgId, msgIdASE)
      //println(pmsgId_ASEkey.get(msgId))

      // encrypt msg
      var tempmsgE :String = " "
      var tempstr1a:String = RSA_Test.encryptRSAPublic(msg, userPuKey.get(uId))
      //println(userPuKey.get(uId))
      var tempstr2a:String = AES_Test.encrypt(msgIdASE,tempstr1a)
      //println(tempstr1a)
      idepm.put(msgId, tempstr2a)
      //println(idepm.get(msgId))
      //println(tempstr2a)
      tempstr2a = tempstr2a.substring(0,tempstr2a.length-1)
      //println(tempstr2a)
      //println(AES_Test.decrypt(msgId_ASEkey.get(msgId),idem.get(msgId)))
      /*if (tempstr1a.length == 44) {
        tempstr1a = tempstr1a.substring(0,tempstr1a.length-1)
        tempmsgE = tempstr1a
      }
      else {
        tempstr1a = tempstr1a.substring(0,tempstr1a.length-2)
        tempmsgE = tempstr1a
      }*/



      //println(tempmsgE)
      //tempmsgE.replace('=', 'a')
      //var temp:String = tempmsgE.substring(0,tempmsgE.length-2)
      //println(temp)
      //println(tempmsgE.indexOf("="))
      //val tempmsgE :String = "aaaa/aaa"
      //println(tempmsgE)
      //tempmsgE.replace('/', 'a')
      //println(tempmsgE)

      pipeline(Post("http://localhost:8080/myFacebookPM/addPM?messageId="+msgId+"&userId="+uId+"&messageFeed="+tempstr2a+"&location="+Random.alphanumeric.take(2).mkString))
      Thread.sleep(20L)

      //pipeline(Post("http://localhost:8080/myFacebookM/addM?messageId=mess"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1+"&messageFeed="+Random.alphanumeric.take(8).mkString+"&location="+Random.alphanumeric.take(2).mkString))
    }
  }




        // generating photos and albums randomly
        for (b <- 0 to numOfUsers/4) {
          for (a <- 0 to p*80/100) {

            // generate photoId
            var phId: String = "ph"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

            // generate uId
            var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
            //println(uId)

            // generate photo
            var ph: String = Random.alphanumeric.take(32).mkString
            //println(ph)

            // generate ASE key
            var phIdASE:String = phId+uId+ph.substring(3,4)
            //println(phIdASE)

            // input photoId and ASE key
            photoId_ASEkey.put(phId, phIdASE)
            //println(photoId_ASEkey.get(phId))

            // encrypt photo (ph)
            var tempphE :String = AES_Test.encrypt(phIdASE,ph)
            idep.put(phId, tempphE)
            //while (tempphE.count(_ == '=') > 1 ){
              //ph = Random.alphanumeric.take(32).mkString
              //tempphE = AES_Test.encrypt(ph,phIdASE)
            //}
            //println(tempphE+"      "+tempphE.length)
            if (tempphE.length == 44){
              tempphE = tempphE.substring(0,tempphE.length-1)
            }
            else {
              tempphE = tempphE.substring(0,tempphE.length-2)
            }
            //println(tempphE)

            pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId="+phId+"&albumId=al"+Random.nextInt(1000)+"&userId="+uId+"&photoReal="+tempphE))
            Thread.sleep(20L)
            //pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId=ph"+Random.nextInt(1000)+"&albumId=al"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
          }
        }



      for (a <- (numOfUsers/4)+1 to 2*numOfUsers/4) {
        for (b <- 0 to p*60/100) {
          // generate photoId
            var phId: String = "ph"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

            // generate uId
            var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
            //println(uId)

            // generate photo
            var ph: String = Random.alphanumeric.take(32).mkString
            //println(ph)

            // generate ASE key
            var phIdASE:String = phId+uId+ph.substring(3,4)
            //println(phIdASE)

            // input photoId and ASE key
            photoId_ASEkey.put(phId, phIdASE)
            //println(photoId_ASEkey.get(phId))

            // encrypt photo (ph)
            var tempphE :String = AES_Test.encrypt(phIdASE,ph)
            idep.put(phId, tempphE)
            //while (tempphE.count(_ == '=') > 1 ){
              //ph = Random.alphanumeric.take(32).mkString
              //tempphE = AES_Test.encrypt(ph,phIdASE)
            //}
            //println(tempphE+"      "+tempphE.length)
            if (tempphE.length == 44){
              tempphE = tempphE.substring(0,tempphE.length-1)
            }
            else {
              tempphE = tempphE.substring(0,tempphE.length-2)
            }
            //println(tempphE)

            pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId="+phId+"&albumId=al"+Random.nextInt(1000)+"&userId="+uId+"&photoReal="+tempphE))
            Thread.sleep(20L)
            //pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId=ph"+Random.nextInt(1000)+"&albumId=al"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
        }
      }
      for (a <- (2*numOfUsers/4)+1 to 3*numOfUsers/4) {
        for (b <- 0 to p*40/100) {
          // generate photoId
            var phId: String = "ph"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

            // generate uId
            var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
            //println(uId)

            // generate photo
            var ph: String = Random.alphanumeric.take(32).mkString
            //println(ph)

            // generate ASE key
            var phIdASE:String = phId+uId+ph.substring(3,4)
            //println(phIdASE)

            // input photoId and ASE key
            photoId_ASEkey.put(phId, phIdASE)
            //println(photoId_ASEkey.get(phId))

            // encrypt photo (ph)
            var tempphE :String = AES_Test.encrypt(phIdASE,ph)
            idep.put(phId, tempphE)
            //while (tempphE.count(_ == '=') > 1 ){
              //ph = Random.alphanumeric.take(32).mkString
              //tempphE = AES_Test.encrypt(ph,phIdASE)
            //}
            //println(tempphE+"      "+tempphE.length)
            if (tempphE.length == 44){
              tempphE = tempphE.substring(0,tempphE.length-1)
            }
            else {
              tempphE = tempphE.substring(0,tempphE.length-2)
            }
            //println(tempphE)

            pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId="+phId+"&albumId=al"+Random.nextInt(1000)+"&userId="+uId+"&photoReal="+tempphE))
            Thread.sleep(20L)
            //pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId=ph"+Random.nextInt(1000)+"&albumId=al"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
        }
      }
      for (a <- (3*numOfUsers/4)+1 to 4*numOfUsers/4) {
        for (b <- 0 to p*20/100) {
          // generate photoId
            var phId: String = "ph"+Random.nextInt(1000)+Random.alphanumeric.take(5).mkString

            // generate uId
            var uId: String = "user"+(Random.nextInt(numOfUsers-1)+1)
            //println(uId)

            // generate photo
            var ph: String = Random.alphanumeric.take(32).mkString
            //println(ph)

            // generate ASE key
            var phIdASE:String = phId+uId+ph.substring(3,4)
            //println(phIdASE)

            // input photoId and ASE key
            photoId_ASEkey.put(phId, phIdASE)
            //println(photoId_ASEkey.get(phId))

            // encrypt photo (ph)
            var tempphE :String = AES_Test.encrypt(phIdASE,ph)
            idep.put(phId, tempphE)
            //while (tempphE.count(_ == '=') > 1 ){
              //ph = Random.alphanumeric.take(32).mkString
              //tempphE = AES_Test.encrypt(ph,phIdASE)
            //}
            //println(tempphE+"      "+tempphE.length)
            if (tempphE.length == 44){
              tempphE = tempphE.substring(0,tempphE.length-1)
            }
            else {
              tempphE = tempphE.substring(0,tempphE.length-2)
            }
            //println(tempphE)

            pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId="+phId+"&albumId=al"+Random.nextInt(1000)+"&userId="+uId+"&photoReal="+tempphE))
            Thread.sleep(20L)
            //pipeline(Post("http://localhost:8080/myFacebookPh/addPh?photoId=ph"+Random.nextInt(1000)+"&albumId=al"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
        }
      }




      // generating friendship links randomly
      for (a <- 0 to numOfUsers/4) {
        for (b <- 0 to f*80/100) {
          pipeline(Post("http://localhost:8080/myFacebookF/addF?friendId=f"+Random.nextInt(1000)+"&user1Id=user"+b+"&user2Id=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }
      for (a <- (numOfUsers/4)+1 to 2*numOfUsers/4) {
        for (b <- 0 to f*60/100) {
          pipeline(Post("http://localhost:8080/myFacebookF/addF?friendId=f"+Random.nextInt(1000)+"&user1Id=user"+b+"&user2Id=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }
      for (a <- (2*numOfUsers/4)+1 to 3*numOfUsers/4) {
        for (b <- 0 to f*40/100) {
          pipeline(Post("http://localhost:8080/myFacebookF/addF?friendId=f"+Random.nextInt(1000)+"&user1Id=user"+b+"&user2Id=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }
      for (a <- (3*numOfUsers/4)+1 to 4*numOfUsers/4) {
        for (b <- 0 to f*20/100) {
          pipeline(Post("http://localhost:8080/myFacebookF/addF?friendId=f"+Random.nextInt(1000)+"&user1Id=user"+b+"&user2Id=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }



      // generating pages randomly
      for (a <- 0 to p) {
        pipeline(Post("http://localhost:8080/myFacebookPa/addPage?pageId=page"+Random.nextInt(1000)+"&msg="+Random.alphanumeric.take(8).mkString))
        Thread.sleep(20L)
      }

      // generating pages and user links randomly
      for (a <- 0 to numOfUsers/4) {
        for (a <- 0 to p*80/100) {
          pipeline(Post("http://localhost:8080/myFacebookPa/addPageUser?pageId=page"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }
      for (a <- (numOfUsers/4)+1 to 2*numOfUsers/4) {
        for (a <- 0 to p*60/100) {
          pipeline(Post("http://localhost:8080/myFacebookPa/addPageUser?pageId=page"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }
      for (a <- (2*numOfUsers/4)+1 to 3*numOfUsers/4) {
        for (a <- 0 to p*40/100) {
          pipeline(Post("http://localhost:8080/myFacebookPa/addPageUser?pageId=page"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }
      for (a <- (3*numOfUsers/4)+1 to 4*numOfUsers/4) {
        for (a <- 0 to p*20/100) {
          pipeline(Post("http://localhost:8080/myFacebookPa/addPageUser?pageId=page"+Random.nextInt(1000)+"&userId=user"+Random.nextInt(numOfUsers-1)+1))
          Thread.sleep(20L)
        }
      }



      // display of profile's private messages
      val result2 = securePipeline(Get("http://localhost:8080/myFacebookPM/myPM"))
      Thread.sleep(300L)
      result2.foreach { response =>
        var mList = ArrayBuffer[String]()
        var mListe = ArrayBuffer[String]()
        var tempstr12 = response.entity.asString

        tempstr12 = tempstr12.substring(56,tempstr12.length)
        //println(tempstr12)
        while (tempstr12.indexOf("(") > -1) {
          var strpos:Int = tempstr12.indexOf("(")
          tempstr12 = tempstr12.substring(strpos+1,tempstr12.length)
          strpos = tempstr12.indexOf(",")
          var msId : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(",")
          var us : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(",")
          var ms : String = tempstr12.substring(0,strpos)

          var astr : String = msId+","+us+","+ms
          //println(astr)
          mList += astr
          //println(pmsgId_ASEkey.get(msId))
          //println(userPrKey.get(us))
          var mse : String = " "
          mse = AES_Test.decrypt(pmsgId_ASEkey.get(msId),idepm.get(msId))
          var mse1 : String = RSA_Test.decryptRSAPrivate(mse,userPrKey.get(us))
          astr = msId+","+us+","+mse1
          mListe += astr

        }
        //println(mList)
        println("The Private Messages of the Client.")
        println(mListe)
        println()

        //println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
      }




      // display of profile's messages
      val result21 = securePipeline(Get("http://localhost:8080/myFacebookM/myM"))
      Thread.sleep(300L)
      result21.foreach { response =>
        var mList = ArrayBuffer[String]()
        var mListe = ArrayBuffer[String]()
        var tempstr12 = response.entity.asString

        tempstr12 = tempstr12.substring(48,tempstr12.length)
        //println(tempstr12)
        while (tempstr12.indexOf("(") > -1) {
          var strpos:Int = tempstr12.indexOf("(")
          tempstr12 = tempstr12.substring(strpos+1,tempstr12.length)
          strpos = tempstr12.indexOf(",")
          var msId : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(",")
          var us : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(",")
          var ms : String = tempstr12.substring(0,strpos)

          var astr : String = msId+","+us+","+ms
          mList += astr
          //println(msgId_ASEkey.get(msId))
          var mse : String = AES_Test.decrypt(msgId_ASEkey.get(msId),idem.get(msId))
          astr = msId+","+us+","+mse
          mListe += astr

        }
        //println(mList)
        println("The Messages of the Client.")
        println(mListe)
        println()

        //println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
      }





  // display of profile's photos
  val result5 = securePipeline(Get("http://localhost:8080/myFacebookPh/myPh"))
  Thread.sleep(300L)
  result5.foreach { response =>
        var phList = ArrayBuffer[String]()
        var phListe = ArrayBuffer[String]()
        var tempstr12 = response.entity.asString

        tempstr12 = tempstr12.substring(46,tempstr12.length)
        //println(tempstr12)
        while (tempstr12.indexOf("(") > -1) {
          var strpos:Int = tempstr12.indexOf("(")
          tempstr12 = tempstr12.substring(strpos+1,tempstr12.length)
          strpos = tempstr12.indexOf(",")
          var phId : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(",")
          var aId : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(",")
          var us : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)
          strpos = tempstr12.indexOf(")")
          var ph : String = tempstr12.substring(0,strpos)
          tempstr12 = tempstr12.substring(strpos+1)

          var astr : String = phId+","+aId+","++us+","+ph
          phList += astr
          //println(photoId_ASEkey.get(phId))
          var ph1:String = ph
          var phe : String = AES_Test.decrypt(photoId_ASEkey.get(phId),idep.get(phId))
          astr = phId+","+aId+","++us+","+phe
          phListe += astr

        }
        //println(phList)
        println("The Photos of the Client.")
        println(phListe)
        println()

    //println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
  }



  // display of profile's friendlist
  val result4 = securePipeline(Get("http://localhost:8080/myFacebookF/myF"))
  Thread.sleep(300L)
  result4.foreach { response =>
    println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
  }


  // display of profile's wall messages
  val result22 = securePipeline(Get("http://localhost:8080/myFacebookM/myMwall"))
  Thread.sleep(300L)
  result22.foreach { response =>
    var mList = ArrayBuffer[String]()
    var mListe = ArrayBuffer[String]()
    var tempstr12 = response.entity.asString

    tempstr12 = tempstr12.substring(48,tempstr12.length)
    //println(tempstr12)
    while (tempstr12.indexOf("(") > -1) {
      var strpos:Int = tempstr12.indexOf("(")
      tempstr12 = tempstr12.substring(strpos+1,tempstr12.length)
      strpos = tempstr12.indexOf(",")
      var msId : String = tempstr12.substring(0,strpos)
      tempstr12 = tempstr12.substring(strpos+1)
      strpos = tempstr12.indexOf(",")
      var us : String = tempstr12.substring(0,strpos)
      tempstr12 = tempstr12.substring(strpos+1)
      strpos = tempstr12.indexOf(",")
      var ms : String = tempstr12.substring(0,strpos)

      var astr : String = msId+","+us+","+ms
      mList += astr
      //println(msgId_ASEkey.get(msId))
      var mse : String = AES_Test.decrypt(msgId_ASEkey.get(msId),idem.get(msId))
      astr = msId+","+us+","+mse
      mListe += astr

    }
    //println(mList)
    println("The Time Line Messages of the Client.")
    println(mListe)
    println()

    //println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
  }



    // deleting a user
    //pipeline(Post("http://localhost:8080/myFacebook/deleteUser?id=user"+Random.nextInt(numOfUsers-1)+1+"&fname="+Random.alphanumeric.take(8).mkString+"&lname="+Random.alphanumeric.take(8).mkString+"&birth="+Random.nextInt(11)+1+"-"+Random.nextInt(29)+1+"-"+Random.nextInt(2015)+"&locationFrom="+Random.alphanumeric.take(2).mkString+"&locationIn="+Random.alphanumeric.take(2).mkString))
    // simlarly there are other deletion procedure for photos, albums, messgaes, pages and other functionality

    // connecting session for a profile
    //pipeline(Post("http://localhost:8080/myFacebook/sessionStart?idg="+"user"+Random.nextInt(numOfUsers-1)+1))

    //Thread.sleep(10L)
    //val result = securePipeline(Get("http://localhost:8080/myFacebook/all"))
    //securePipeline(Get("http://localhost:8080/list/all"))
    //Thread.sleep(300L)
    //result.foreach { response =>
      //println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
    //}



    // display of profile details
    val result1 = securePipeline(Get("http://localhost:8080/myFacebook/profileDetails"))
    Thread.sleep(300L)
    result1.foreach { response =>
      println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
    }



   // display of profile's pages
   val result3 = securePipeline(Get("http://localhost:8080/myFacebookPa/MyPages"))
   Thread.sleep(300L)
   result3.foreach { response =>
     println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
     println()
   }




   // display of profile's albums
   val result6 = securePipeline(Get("http://localhost:8080/myFacebookPh/myAl"))
   Thread.sleep(300L)
   result6.foreach { response =>
     println(s"Request completed with status ${response.status} and content:\n${response.entity.asString}")
     println()
   }



//pipeline(Post("http://localhost:8080/amber/add/mined?country=UnitedKingdom&size=50"))

Thread.sleep(1000L)

system.shutdown()
system.awaitTermination()


}

