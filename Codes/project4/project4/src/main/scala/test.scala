
import java.security._
import java.util
import javax.crypto.{KeyGenerator, SecretKey, Cipher}
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.nio.ByteBuffer
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import spray.can.client._
import spray.http.MediaTypes
import spray.json.DefaultJsonProtocol
import spray.routing.{RequestContext, Route, SimpleRoutingApp}
import sun.misc.{BASE64Encoder, BASE64Decoder}
import scala.collection.mutable
import scala.concurrent.duration._
import akka.pattern.ask
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import java.util


object test extends App with SimpleRoutingApp {
  implicit lazy val actorSystem = ActorSystem()

  var profileArrayBuffer = Amber.ambers
  var messageArrayBuffer = AmberM.ambersm
  var pmessageArrayBuffer = AmberPM.amberspm
  //var tempmessageArrayBuffer = AmberM.ambersm
  var friendArrayBuffer = AmberF.ambersf
  var photoArrayBuffer = AmberPh.ambersph
  var pageArrayBuffer = AmberPage.amberspage
  var pageuserArrayBuffer = AmberPageUser.amberspageuser
  //var eventArrayBuffer = AmberE.amberse

  //val jmap = new java.util.HashMap[String, String]()
  //jmap.put("fname", "Alvin")
  //jmap.put("lname", "Alexander")
  //println(jmap.get("fname"))

  var KeyPair = RSA_Test.generateKey
  var serverPrivateKey: PrivateKey = KeyPair.getPrivate()
  var serverPublicKey: PublicKey = KeyPair.getPublic()

  var currentUser:Int = 1
  var currentUserId:String = " "
  var currentPubKey:String = " "
  var UserSentPublicKey:PublicKey = serverPublicKey
  var tokenGenerated: String = "userpassword"
  var mId:String = " "
  var fId:String = " "
  var phId:String = " "
  var pId:String = " "
  var alId:String = " "
  //var fList1 = ArrayBuffer[String]()
  var switchFunction:Int = 0
  //var mList = ArrayBuffer[String]()
  var fListG = ArrayBuffer[String]()
  //var phList = ArrayBuffer[String]()
  //var alList = ArrayBuffer[String]()
  //var alphList = ArrayBuffer[String]()
  var userPuKey = new java.util.HashMap[String, String]()
  var userPuKeyHash = new java.util.HashMap[String, String]()
  var msgId_ASEkey = new java.util.HashMap[String, String]()
  var pmsgId_ASEkey = new java.util.HashMap[String, String]()
  var photoId_ASEkey = new java.util.HashMap[String, String]()

  implicit val timeout = Timeout(1.second)
  import actorSystem.dispatcher

  lazy val helloActor = actorSystem.actorOf(Props(new HelloActor()))
  lazy val miningActor = actorSystem.actorOf(Props(new MiningActor()))

  lazy val operatorFB = actorSystem.actorOf(Props(new OperatorFB()))
  lazy val MoperatorFB = actorSystem.actorOf(Props(new MOperatorFB()))
  lazy val MoperatorFB1 = actorSystem.actorOf(Props(new MOperatorFB1()))
  lazy val PMoperatorFB1 = actorSystem.actorOf(Props(new PMOperatorFB1()))
  lazy val MoperatorFB2 = actorSystem.actorOf(Props(new MOperatorFB2()))
  lazy val FoperatorFB = actorSystem.actorOf(Props(new FOperatorFB()))
  lazy val FoperatorFB1 = actorSystem.actorOf(Props(new FOperatorFB1()))

  lazy val PhoperatorFB = actorSystem.actorOf(Props(new PhOperatorFB()))
  lazy val PhoperatorFB1 = actorSystem.actorOf(Props(new PhOperatorFB1()))
  lazy val AloperatorFB = actorSystem.actorOf(Props(new AlOperatorFB()))
  lazy val AloperatorFB1 = actorSystem.actorOf(Props(new AlOperatorFB1()))
  lazy val AlPhoperatorFB1 = actorSystem.actorOf(Props(new AlPhOperatorFB1()))

  lazy val KeyoperatorFB1 = actorSystem.actorOf(Props(new KeyOperatorFB1()))
  lazy val KeyoperatorFB2 = actorSystem.actorOf(Props(new KeyOperatorFB2()))
  lazy val KeyoperatorFB3 = actorSystem.actorOf(Props(new KeyOperatorFB3()))


  lazy val operatorFBAu = actorSystem.actorOf(Props(new OperatorFBAu()))
  var tokenReceived:String = RSA_Test.encryptRSAPrivate(tokenGenerated,serverPrivateKey)
  lazy val PageoperatorFB = actorSystem.actorOf(Props(new PageOperatorFB()))
  lazy val PageoperatorFB1 = actorSystem.actorOf(Props(new PageOperatorFB1()))

  def getJson(route: Route) = get {
    respondWithMediaType(MediaTypes.`application/json`) {
      route
    }
  }

  //idDelete += 1
  //idDelete += 1

  //println(profileArrayBuffer(0))
  //println(messageArrayBuffer(0))
  //println(friendArrayBuffer(0))
  //println(photoArrayBuffer(0))
  //println(pageuserArrayBuffer(0))
  //println(pageArrayBuffer(0))
  var tokenToBeSentE: String = " "

  startServer(interface = "localhost", port = 8080){
    amberRoute ~ miningRoute
    //amberRouteM ~ miningRoute
  }

  lazy val amberRoute = {
    /* ======================================== Private Message Module ======================================================== */
    get { // testing
      path("myFacebookPM" / "hello") {
        complete {
          "Welcome to myFacebook Private Message Module"
        }
      }
    }~
      get {
        path("myFacebookPM" / "myPM") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")

            // Call a actor to handle this
            //MoperatorFB1 ! sentmyM

            complete {
              (PMoperatorFB1 ? sentmyPM).mapTo[ArrayBuffer[String]]
                .map(s => s"The Current User's Private Message(s) are:\n $s")
              //Thread.sleep(10000L)
              //AmberM.toJson(messageArrayBuffer)
              //"The Message List has been Retrived 1"
            }
          }
        }
      }~
      post{
        path("myFacebookPM" / "addPM") {
          parameters("messageId".as[String], "userId".as[String], "messageFeed".as[String], "location".as[String]) { (messageId, userId, messageFeed, location) =>

            val newAmberM = fbFeedpm(messageId, userId, messageFeed, location)
            pmessageArrayBuffer += newAmberM

            complete {
              "Private Message Added Successfully"
            }
          }
        }
      }~
      /* ======================================== Key Saving Module ======================================================== */
      post{
        path("myFacebookKey" / "addKey") {
          parameters("Id".as[String], "keyStr".as[String], "keyType".as[String]) { (Id, keyStr, keyType) =>

            if (keyType.equals("1")) {
              msgId_ASEkey.get(Id,keyStr)
            }
            else if (keyType.equals("2")) {
              pmsgId_ASEkey.get(Id,keyStr)
            }
            else if (keyType.equals("3")) {
              photoId_ASEkey.get(Id,keyStr)
            }
            else if (keyType.equals("4")) {
              userPuKey.get(Id,keyStr)
              // do hash
              var temphash:String = RSA_Test.toHash(keyStr)
              userPuKeyHash.get(Id,temphash)
            }

            complete {
              "Keys Added Successfully"
            }
          }
        }
      }~
      get {
        path("myFacebookKey" / "mymKey") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")

            // Call a actor to handle this
            //MoperatorFB1 ! sentmyM

            complete {
              (KeyoperatorFB1 ? sentmymKey).mapTo[ArrayBuffer[String]]
                .map(s => s"The Current User's Private Message(s) are:\n $s")
              //Thread.sleep(10000L)
              //AmberM.toJson(messageArrayBuffer)
              //"The Message List has been Retrived 1"
            }
          }
        }
      }~
      get {
        path("myFacebookKey" / "mypmKey") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")

            // Call a actor to handle this
            //MoperatorFB1 ! sentmyM

            complete {
              (KeyoperatorFB2 ? sentmypmKey).mapTo[ArrayBuffer[String]]
                .map(s => s"The Current User's Private Message(s) are:\n $s")
              //Thread.sleep(10000L)
              //AmberM.toJson(messageArrayBuffer)
              //"The Message List has been Retrived 1"
            }
          }
        }
      }~
      get {
        path("myFacebookKey" / "mypKey") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")

            // Call a actor to handle this
            //MoperatorFB1 ! sentmyM

            complete {
              (KeyoperatorFB3 ? sentmypKey).mapTo[ArrayBuffer[String]]
                .map(s => s"The Current User's Private Message(s) are:\n $s")
              //Thread.sleep(10000L)
              //AmberM.toJson(messageArrayBuffer)
              //"The Message List has been Retrived 1"
            }
          }
        }
      }~
    /* ======================================== Authentication Module ======================================================== */
          get { // testing
            path("myFacebookAu" / "hello") {
              complete {
                "Welcome to myFacebook Authentication Module"
              }
            }
          }~
          post{
            path("myFacebookAu" / "publicKeySent") {
              parameter("sentpubKeyUser".as[String], "sentuserId".as[String]) { (sentpubKeyUser, sentuserId) =>

                currentUserId = sentuserId
                currentPubKey = sentpubKeyUser
                var UserSentPublicKey1: PublicKey = RSA_Test.decodePubKey(currentPubKey)
                //tokenToBeSentE = RSA_Test.encryptRSAPublic(tokenGenerated,UserSentPublicKey)
                //println(UserSentPublicKey)
                //operatorFBAu ! transform
                /*var encoder : BASE64Encoder = new BASE64Encoder()
                var str: String = encoder.encode(sentpubKeyUser.getBytes());
                var decoder : BASE64Decoder = new BASE64Decoder()
                var arr1: Array[Byte]= decoder.decodeBuffer(str)
                var x509KeySpec: X509EncodedKeySpec = new X509EncodedKeySpec(arr1)
                var keyFact:KeyFactory = KeyFactory.getInstance("RSA");
                UserSentPublicKey = keyFact.generatePublic(x509KeySpec)*/
                //println(UserSentPublicKey) */
                complete {
                  "Recieved Public Key of User Successfully"
                }
              }
            }
          }~
          get {
            path("myFacebookAu" / "getToken") {
              var tokenGenerated:String = Random.alphanumeric.take(8).mkString

              //println("hi "+UserSentPublicKey)
              tokenToBeSentE = RSA_Test.encryptRSAPublic(tokenGenerated,UserSentPublicKey)
              //var tokenToBeSentEs: String = tokenToBeSentE.toString
              //var i1:String = tokenToBeSentE.length.toString

              complete {
                tokenToBeSentE
              }
            }
          }~
          post{
            path("myFacebookAu" / "ETokenSent") {
              parameter("sentEToken".as[String]) { (sentEToken) =>

                // decrprt with private key of server and match with token

                val receivedToken: String = RSA_Test.decryptRSAPublic(tokenReceived,UserSentPublicKey)

                var tempStr:String = " "
                if (receivedToken.equals(tokenGenerated)) {
                  tempStr = "Successful"

                  // find the index for currentUser
                  switchFunction = 1
                  operatorFB ! findcurrentUser
                }
                else {
                  tempStr = "Failure"
                }

                var finalstr:String = "Session Start is "+" "+tempStr
                println("User Authentication Complete")

                complete {
                  finalstr
                }
              }
            }
          }~
    /* ======================================== Profile Module ======================================================== */
      get { // testing
        path("myFacebook" / "hello") {
          complete {
            "Welcome to myFacebook Profile Module"
          }
        }
      } ~
      get {  // display all profile
        path("myFacebook" / "all") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")
            complete {
              Amber.toJson(profileArrayBuffer)
            }
          }
        }
      } ~
      get {
        path ("myFacebook" / "profileDetails") {
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              Amber.toJson(profileArrayBuffer(currentUser))
              //Amber.toJson(profileArrayBuffer(index))
            }
          }
        }
      } ~
      post{
        path("myFacebook" / "sessionStart") {
          parameter("idg".as[String]) { (idg) =>
            currentUserId = idg

            // find the index for currentUser
            switchFunction = 1
            operatorFB ! findcurrentUser

            complete {
              "Session Started Successfully for User"
            }
          }
        }
      }~
      post{
        path("myFacebook" / "addUser"){
          //parameters("country"?, "size".as[Int]) { (country, size) =>
          parameters("id".as[String], "fname".as[String], "lname".as[String], "birth".as[String], "locationFrom".as[String], "locationIn".as[String], "token".as[String]) { (id, fname, lname, birth, locationFrom, locationIn, token) =>
            //val newAmber = fbprofile(fname = "XYZ1", lname = "Sur1", birth = "01-01-2015", locationFrom = "India", locationIn = "US")
            //profileArrayBuffer += newAmber
            val newAmber = fbprofile(id, fname, lname, birth, locationFrom, locationIn, token)
            profileArrayBuffer += newAmber

            //profileArrayBuffer.remove(0)
            //val newAmber1 = Amber.toJson(profileArrayBuffer(index))
            //(newAmber1)

            // add a A

            complete{
              "User Added Successfully"
            }
          }
        }
      }~
      post{
        path("myFacebook" / "updateUser") {
          parameters("id".as[String], "fname".as[String], "lname".as[String], "birth".as[String], "locationFrom".as[String], "locationIn".as[String], "token".as[String]) { (id, fname, lname, birth, locationFrom, locationIn, token) =>

            // create session
            currentUserId = id
            switchFunction = 0
            operatorFB ! findcurrentUser

            // Search and Remove
            //println("hi "+currentUser)
            //profileArrayBuffer.remove(currentUser)

            // Add a New
            val newAmber = fbprofile(id, fname, lname, birth, locationFrom, locationIn, token)
            profileArrayBuffer += newAmber
            complete {
              "User Updated Successfully"
            }
          }
        }
      }~
      post{
        path("myFacebook" / "deleteUser") {
          parameters("id".as[String], "fname".as[String], "lname".as[String], "birth".as[String], "locationFrom".as[String], "locationIn".as[String], "token".as[String]) { (id, fname, lname, birth, locationFrom, locationIn, token) =>
            // create session
            currentUserId = id
            switchFunction = 0
            operatorFB ! findcurrentUser

            // Remove
            //profileArrayBuffer.remove(currentUser)

            complete {
              "User deleted Successfully"
            }
          }
        }
      }~
      /* ======================================== Message Module ======================================================== */
      get {
        path("myFacebookM" / "helloM") {
          complete {
            "Welcome to myFacebook Message Module"
          }
        }
      }~
      get {
        path("myFacebookM" / "allM") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")
            complete {
              AmberM.toJson(messageArrayBuffer)
            }
          }
        }
      }~
      get {
        path("myFacebookM" / "myM") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")

            // Call a actor to handle this
            //MoperatorFB1 ! sentmyM

            complete {
              (MoperatorFB1 ? sentmyM).mapTo[ArrayBuffer[String]]
                .map(s => s"The Current User's Message(s) are:\n $s")
              //Thread.sleep(10000L)
              //AmberM.toJson(messageArrayBuffer)
              //"The Message List has been Retrived 1"
            }
          }
        }
      }~
        post{
          path("myFacebookM" / "addM") {
            parameters("messageId".as[String], "userId".as[String], "messageFeed".as[String], "location".as[String]) { (messageId, userId, messageFeed, location) =>

                val newAmberM = fbFeed(messageId, userId, messageFeed, location)
                messageArrayBuffer += newAmberM

              complete {
                "Message Added Successfully"
              }
            }
          }
        }~
        post{
          path("myFacebookM" / "deleteM") {
            parameters("messageId".as[String], "userId".as[String]) { (messageId, userId) =>
              // create session
              currentUserId = userId
              mId = messageId
              //switchFunction = 0
              MoperatorFB ! findcurrentM

              complete {
                "Message Deleted Successfully"
              }
            }
          }
        }~
        get {
          path("myFacebookM" / "myMwall") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")

              // Call a actor to handle this
              //MoperatorFB1 ! sentmyM

              complete {
                (MoperatorFB2 ? sentmyM2).mapTo[ArrayBuffer[String]]
                  .map(s => s"The Current User's Message(s) are:\n $s")
                //Thread.sleep(10000L)
                //AmberM.toJson(messageArrayBuffer)
                //"The Message List has been Retrived 1"
              }
            }
          }
        }~
        /* ======================================== Page Module ======================================================== */
        get {
          path("myFacebookPa" / "helloPa") {
            complete {
              "Welcome to myFacebook Page Module"
            }
          }
        }~
        get {
          path("myFacebookPa" / "allPa") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                AmberPage.toJson(pageArrayBuffer)
              }
            }
          }
        }~
        get {
          path("myFacebookPa" / "allPaU") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                AmberPageUser.toJson(pageuserArrayBuffer)
              }
            }
          }
        }~
        post{
          path("myFacebookPa" / "addPage") {
            parameters("pageId".as[String], "msg".as[String]) { (pageId, msg) =>

                val newAmberPage = fbPage(pageId, msg)
                pageArrayBuffer += newAmberPage

              complete {
                "Page Added Successfully"
              }
            }
          }
        }~
        post{
          path("myFacebookPa" / "addPageUser") {
            parameters("pageId".as[String], "userId".as[String]) { (pageId, userId) =>

                val newAmberPageUser = fbPageUser(pageId, userId)
                pageuserArrayBuffer += newAmberPageUser

              complete {
                "Page and User Pair Added Successfully"
              }
            }
          }
        }~
        get {
          path("myFacebookPa" / "MyPages") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                (PageoperatorFB1 ? sentmyPage).mapTo[ArrayBuffer[String]]
                  .map(s => s"The Current User's Page(s) are:\n $s")
              }
            }
          }
        }~
        post{
          path("myFacebookPa" / "deletePageUser") {
            parameters("pageId".as[String], "userId".as[String]) { (pageId, userId) =>

              // create session
              currentUserId = userId
              pId = pageId
              //switchFunction = 0
              PageoperatorFB ! findcurrentPage

              complete {
                "Friendship Status Deleted Successfully"
              }
            }
          }
        }~
        /* ======================================== Friendship Module ======================================================== */
        get {
          path("myFacebookF" / "helloF") {
            complete {
              "Welcome to myFacebook Friendship Module"
            }
          }
        }~
        get {
          path("myFacebookF" / "allF") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                AmberF.toJson(friendArrayBuffer)
              }
            }
          }
        }~
        get {
          path("myFacebookF" / "myF") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                (FoperatorFB1 ? sentmyF).mapTo[ArrayBuffer[String]]
                  .map(s => s"The Current User's Friend(s) are:\n $s")
                //Thread.sleep(10000L)
                //AmberM.toJson(messageArrayBuffer)
                //"The Message List has been Retrived 1"
              }
            }
          }
        }~
        post{
          path("myFacebookF" / "addF") {
            parameters("friendId".as[String], "user1Id".as[String], "user2Id".as[String]) { (friendId, user1Id, user2Id) =>

              if (!user1Id.equals(user2Id)) {
                val newAmberF = fbFriend(friendId, user1Id, user2Id)
                friendArrayBuffer += newAmberF
              }

              complete {
                "Friendship Status Added Successfully"
              }
            }
          }
        }~
        post{
          path("myFacebookF" / "deleteF") {
            parameters("user1Id".as[String], "user2Id".as[String]) { (user1Id, user2Id) =>

              // create session
              currentUserId = user1Id
              fId = user2Id
              //switchFunction = 0
              FoperatorFB ! findcurrentF

              complete {
                "Friendship Status Deleted Successfully"
              }
            }
          }
        }~
      /* ======================================== Photo & Album Module ======================================================== */
      get {
        path("myFacebookPh" / "helloPh") {
          complete {
            "Welcome to myFacebook Photo & Album Module"
          }
        }
      }~
      get {
        path("myFacebookPh" / "allPh") {
          respondWithMediaType(MediaTypes.`application/json`) {
            //println("received list all")
            complete {
              AmberPh.toJson(photoArrayBuffer)
            }
          }
        }
      }~
        get {
          path("myFacebookPh" / "myPh") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                (PhoperatorFB1 ? sentmyPh).mapTo[ArrayBuffer[String]]
                  .map(s => s"The Current User's Photo(s) are:\n $s")
                //Thread.sleep(10000L)
                //AmberM.toJson(messageArrayBuffer)
                //"The Message List has been Retrived 1"
              }
            }
          }
        }~
        get {
          path("myFacebookPh" / "myAl") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                (AloperatorFB1 ? sentmyAl).mapTo[ArrayBuffer[String]]
                  .map(s => s"The Current User's Album(s) are:\n $s")
                //Thread.sleep(10000L)
                //AmberM.toJson(messageArrayBuffer)
                //"The Message List has been Retrived 1"
              }
            }
          }
        }~
        get {
          path("myFacebookPh" / "AlbumDetails") {
            respondWithMediaType(MediaTypes.`application/json`) {
              //println("received list all")
              complete {
                (AlPhoperatorFB1 ? sentmyAlPh).mapTo[ArrayBuffer[String]]
                  .map(s => s"The Album Details are:\n $s")
                //Thread.sleep(10000L)
                //AmberM.toJson(messageArrayBuffer)
                //"The Message List has been Retrived 1"
              }
            }
          }
        }~
        post{
          path("myFacebookPh" / "addPh") {
            parameters("photoId".as[String], "albumId".as[String], "userId".as[String], "photoReal".as[String]) { (photoId, albumId, userId, photoReal) =>

              val newAmberPh = fbPhoto(photoId, albumId, userId, photoReal)
              photoArrayBuffer += newAmberPh

              complete {
                "Photo Added Successfully"
              }
            }
          }
        }~
        post{
          path("myFacebookPh" / "deletePh") {
            parameters("photoId".as[String], "userId".as[String]) { (photoId, userId) =>

              // create session
              currentUserId = userId
              phId = photoId
              //switchFunction = 0
              PhoperatorFB ! findcurrentPh

              complete {
                "Photo Deleted Successfully"
              }
            }
          }
        }~
        post{
          path("myFacebookPh" / "deleteAl") {
            parameters("albumId".as[String], "userId".as[String]) { (albumId, userId) =>

              // create session
              currentUserId = userId
              alId = albumId
              //switchFunction = 0
              AloperatorFB ! findcurrentAl

              complete {
                "Album Deleted Successfully"
              }
            }
          }
        }

  }

  /* ======================================== Profile Module ======================================================== */
  class OperatorFBAu extends Actor {
    override def receive = {
      case transform => {

        println(currentPubKey)
        currentPubKey = '['+currentPubKey
        println(currentPubKey)
        //var temppubKey:Array[Byte] = currentPubKey.getBytes()

        //var encoder : BASE64Encoder = new BASE64Encoder()
        //var str: String = encoder.encode(currentPubKey.getBytes());

        //var str: String = currentPubKey

        //var decoder : BASE64Decoder = new BASE64Decoder()
        //var arr1: Array[Byte] = decoder.decodeBuffer(currentPubKey)

        //var arr1: Array[Byte] = str.getBytes()

        //var x509KeySpec: X509EncodedKeySpec = new X509EncodedKeySpec(arr1)
        //var keyFact:KeyFactory = KeyFactory.getInstance("RSA");
        //UserSentPublicKey = keyFact.generatePublic(x509KeySpec)
        //println(UserSentPublicKey)

      }
    }
  }

  /* ======================================== Private Message Module ======================================================== */
  class PMOperatorFB1 extends Actor {
    override def receive = {
      case sentmyPM => {
        var mList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to pmessageArrayBuffer.length-1) {

          var strTempm:String = pmessageArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(7,strTmpIndex)
          strTmpIndex = strTempu.indexOf(',')
          strTempu = strTempu.substring(0,strTmpIndex)

          if (currentUserId1.equals(strTempu)) {
            var strTempm1:String = pmessageArrayBuffer(a).toString()
            mList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        mList = mList.reverse
        sender ! mList
      }
    }
  }
  /* ======================================== Profile Module ======================================================== */
  class OperatorFB extends Actor {
    override def receive = {
      case findcurrentUser => {
        //finding currentUser
        //testing
        //println("actor "+profileArrayBuffer.length)
        var currentUserId1:String = currentUserId
        var temp:Int = 0
        for (a <- 0 to profileArrayBuffer.length-1) {
          var strTemp:String = profileArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp.indexOf(',')
          strTemp = strTemp.substring(10,strTmpIndex)
          //println(strTemp)
          if (currentUserId1.equals(strTemp) /*&& temp == 0*/) {
            currentUser = a
            temp = 1
          }
        }
        //sender ! currentUser
        //println("hi "+currentUser)
        if (temp == 1 && switchFunction == 0) {
          profileArrayBuffer.remove(currentUser)
        }
      }
    }
  }
  /* ======================================== Key Module ======================================================== */
  class KeyOperatorFB1 extends Actor {
    override def receive = {
      case sentmymKey => {
        var mList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to messageArrayBuffer.length-1) {

          var strTempm:String = messageArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')

          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(7,strTmpIndex)
          var msgIdtemp:String = strTempm
          strTmpIndex = strTempu.indexOf(',')
          strTempu = strTempu.substring(0,strTmpIndex)

          if (currentUserId1.equals(strTempu)) {
            var strTempm1:String = (msgIdtemp+","+msgId_ASEkey.get(msgIdtemp))
            mList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        mList = mList.reverse
        sender ! mList
      }
    }
  }

  class KeyOperatorFB2 extends Actor {
    override def receive = {
      case sentmypmKey => {
        var mList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to pmessageArrayBuffer.length-1) {

          var strTempm:String = pmessageArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(9,strTmpIndex)
          var pmsgIdtemp:String = strTempm
          strTmpIndex = strTempu.indexOf(',')
          strTempu = strTempu.substring(0,strTmpIndex)

          if (currentUserId1.equals(strTempu)) {
            var strTempm1:String = (pmsgIdtemp+","+pmsgId_ASEkey.get(pmsgIdtemp))
            mList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        mList = mList.reverse
        sender ! mList
      }
    }
  }

  class KeyOperatorFB3 extends Actor {
    override def receive = {
      case sentmypKey => {
        var phList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to photoArrayBuffer.length-1) {

          var strTemp1:String = photoArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(8,strTmpIndex)
          var psgIdtemp:String = strTemp1
          strTmpIndex = strTemp2.indexOf(',')
          var strTemp3:String = strTemp2.substring(strTmpIndex+1,strTemp2.length)
          strTemp2 = strTemp2.substring(0,strTmpIndex)
          strTmpIndex = strTemp3.indexOf(',')
          strTemp3 = strTemp3.substring(0,strTmpIndex)
          //println(strTemp1+" "+strTemp2+" "+strTemp3) // testing

          if (currentUserId1.equals(strTemp3)) {
            var strTempm1:String = (psgIdtemp+","+photoId_ASEkey.get(psgIdtemp))
            phList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        //phList = phList.reverse
        phList = phList.distinct
        //println(phList)
        sender ! phList
      }
    }
  }
  /* ======================================== Message Module ======================================================== */
  class MOperatorFB extends Actor {
    override def receive = {
      case findcurrentM => {
        //finding currentUser
        //testing
        //println("actor "+profileArrayBuffer.length)
        var currentUserId1:String = currentUserId
        var mId1:String = mId
        var temp:Int = 0
        var currentM:Int = 0
        for (a <- 0 to messageArrayBuffer.length-1) {

          var strTempm:String = messageArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(7,strTmpIndex)
          strTmpIndex = strTempu.indexOf(',')
          strTempu = strTempu.substring(0,strTmpIndex)
          //println(strTempm+" "+strTempu) // testing
          if (currentUserId1.equals(strTempu) && mId1.equals(strTempm) /*&& temp == 0*/) {
            currentM = a
            temp = 1
          }
        }
        //sender ! currentUser
        //println("hi "+currentM)
        if (temp == 1) {
          messageArrayBuffer.remove(currentM)
        }
      }
    }
  }

  class MOperatorFB1 extends Actor {
    override def receive = {
      case sentmyM => {
        var mList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to messageArrayBuffer.length-1) {

          var strTempm:String = messageArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(7,strTmpIndex)
          strTmpIndex = strTempu.indexOf(',')
          strTempu = strTempu.substring(0,strTmpIndex)

          if (currentUserId1.equals(strTempu)) {
            var strTempm1:String = messageArrayBuffer(a).toString()
            mList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        mList = mList.reverse
        sender ! mList
      }
    }
  }

  class MOperatorFB2 extends Actor {
    override def receive = {
      case sentmyM2 => {

        // finding friendlist
        //var myfriendlist1 = FoperatorFB1 ! sentmyF

        //println(fListG)

        // finding the message list
        var mList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to messageArrayBuffer.length-1) {

          var strTempm:String = messageArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(7,strTmpIndex)
          strTmpIndex = strTempu.indexOf(',')
          strTempu = strTempu.substring(0,strTmpIndex)

          //println(strTempu)
          if (currentUserId1.equals(strTempu)) {
            var strTempm1:String = messageArrayBuffer(a).toString()
            mList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          else {
            for (b <- 0 to fListG.length-1) {
              if (strTempu.equals(fListG(b))) {
                var strTempm1:String = messageArrayBuffer(a).toString()
                mList += strTempm1
                //println(messageArrayBuffer(a).toString())
              }
            }
          }
          //println(a)
        }
        mList = mList.reverse
        sender ! mList
      }
    }
  }
  /* ======================================== Page Module ======================================================== */
  class PageOperatorFB extends Actor {
    override def receive = {
      case findcurrentPage => {
        //finding currentUser
        //testing
        //println("actor "+profileArrayBuffer.length)
        var currentUserId1:String = currentUserId
        var pId1:String = pId
        var temp:Int = 0
        var currentM:Int = 0
        for (a <- 0 to pageuserArrayBuffer.length-1) {

          var strTempm:String = pageuserArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(11,strTmpIndex)
          //println(strTempm+" "+strTempu) // testing

          if (currentUserId1.equals(strTempu) && pId1.equals(strTempm) /*&& temp == 0*/) {
            currentM = a
            temp = 1
          }
        }
        //sender ! currentUser
        //println("hi "+currentM)
        if (temp == 1) {
          pageuserArrayBuffer.remove(currentM)
        }
      }
    }
  }

  class PageOperatorFB1 extends Actor {
    override def receive = {
      case sentmyPage => {
        var pageList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId

        for (a <- 0 to pageuserArrayBuffer.length-1) {

          var strTempm:String = pageuserArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTempm.indexOf(',')
          var strTempu:String = strTempm.substring(strTmpIndex+1,strTempm.length-1)
          strTempm = strTempm.substring(11,strTmpIndex)
          //println(strTempm+" "+strTempu) // testing

          if (currentUserId1.equals(strTempu) /*&& temp == 0*/) {
            pageList += strTempm
          }
        }
        pageList = pageList.distinct

        var pageMList = ArrayBuffer[String]()
        for (a <- 0 to pageArrayBuffer.length-1) {

          var strTemp1:String = pageArrayBuffer(a).toString()
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(7,strTmpIndex)
          //println(strTemp1+" "+strTemp2) // testing

          for (b <- 0 to pageList.length-1) {
            if (pageList(b).equals(strTemp1)) {
              var strTempm1:String = pageArrayBuffer(a).toString()
              pageMList += strTempm1
            }
          }
        }
        //pageMList = pageMList.distinct
        sender ! pageMList
      }
    }
  }


  /* ======================================== Friendship Module ======================================================== */
  class FOperatorFB extends Actor {
    override def receive = {
      case findcurrentF => {
        //finding currentUser
        //testing
        //println("actor "+profileArrayBuffer.length)
        var currentUserId1:String = currentUserId
        var fId1:String = fId
        var temp:Int = 0
        var currentM:Int = 0
        for (a <- 0 to friendArrayBuffer.length-1) {

          var strTemp1:String = friendArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          strTemp1 = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTmpIndex = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length)
          strTemp1 = strTemp1.substring(0,strTmpIndex)
          //strTmpIndex = strTemp2.indexOf(',')
          //strTemp2 = strTemp2.substring(0,strTemp2.length-2)
          //println(strTemp1+" "+strTemp2) // testing
          if ( ( (currentUserId1.equals(strTemp1) && fId1.equals(strTemp2)) || (currentUserId1.equals(strTemp2) && fId1.equals(strTemp1))) /*&& temp == 0*/) {
            currentM = a
            temp = 1
          }
        }
        //sender ! currentUser
        //println("hi "+currentM)
        if (temp == 1) {
          friendArrayBuffer.remove(currentM)
        }
      }
    }
  }

  class FOperatorFB1 extends Actor {
    override def receive = {
      case sentmyF => {
        var currentUserId1:String = currentUserId
        var fList = ArrayBuffer[String]()
        for (a <- 0 to friendArrayBuffer.length-1) {

          var strTemp1:String = friendArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          strTemp1 = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTmpIndex = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length)
          strTemp1 = strTemp1.substring(0,strTmpIndex)
          //strTmpIndex = strTemp2.indexOf(',')
          //strTemp2 = strTemp2.substring(0,strTemp2.length-2)
          //println(strTemp1+" "+strTemp2) // testing

          if (currentUserId1.equals(strTemp1)) {
            var strTempm1:String = strTemp2
            fList += strTempm1
            //fListG += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          else if (currentUserId1.equals(strTemp2)) {
            var strTempm1: String = strTemp1
            fList += strTempm1
            //fListG += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        //fList = fList.reverse
        fListG = fList
        fList = fList.distinct
        //println(fListG)
        sender ! fList
      }
    }
  }
  /* ======================================== Photo Module ======================================================== */
  class PhOperatorFB extends Actor {
    override def receive = {
      case findcurrentPh => {
        //finding currentUser
        //testing
        //println("actor "+profileArrayBuffer.length)
        var currentUserId1:String = currentUserId
        var phId1:String = phId
        var temp:Int = 0
        var currentM:Int = 0
        for (a <- 0 to photoArrayBuffer.length-1) {

          var strTemp1:String = photoArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(8,strTmpIndex)
          strTmpIndex = strTemp2.indexOf(',')
          var strTemp3:String = strTemp2.substring(strTmpIndex+1,strTemp2.length)
          strTemp2 = strTemp2.substring(0,strTmpIndex)
          //println(strTemp1+" "+strTemp2+" "+strTemp3) // testing

          if (currentUserId1.equals(strTemp3) && phId1.equals(strTemp1) /*&& temp == 0*/) {
            currentM = a
            temp = 1
          }
        }
        //sender ! currentUser
        //println("hi "+currentM)
        if (temp == 1) {
          photoArrayBuffer.remove(currentM)
        }
      }
    }
  }

  class PhOperatorFB1 extends Actor {
    override def receive = {
      case sentmyPh => {
        var phList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to photoArrayBuffer.length-1) {

          var strTemp1:String = photoArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(8,strTmpIndex)
          strTmpIndex = strTemp2.indexOf(',')
          var strTemp3:String = strTemp2.substring(strTmpIndex+1,strTemp2.length)
          strTemp2 = strTemp2.substring(0,strTmpIndex)
          strTmpIndex = strTemp3.indexOf(',')
          strTemp3 = strTemp3.substring(0,strTmpIndex)
          //println(strTemp1+" "+strTemp2+" "+strTemp3) // testing

          if (currentUserId1.equals(strTemp3)) {
            var strTempm1:String = photoArrayBuffer(a).toString()
            phList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        //phList = phList.reverse
        phList = phList.distinct
        //println(phList)
        sender ! phList
      }
    }
  }
  /* ======================================== Album Module ======================================================== */
  class AlOperatorFB extends Actor {
    override def receive = {
      case findcurrentAl => {
        //finding currentUser
        //testing
        //println("actor "+profileArrayBuffer.length)
        var temp:Int = 0
        var currentUserId1:String = currentUserId
        var alId1:String = alId
        var currentM:Int = 0
        for (a <- 0 to photoArrayBuffer.length-1) {

          var strTemp1:String = photoArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(8,strTmpIndex)
          strTmpIndex = strTemp2.indexOf(',')
          var strTemp3:String = strTemp2.substring(strTmpIndex+1,strTemp2.length)
          strTemp2 = strTemp2.substring(0,strTmpIndex)
          //println(strTemp1+" "+strTemp2+" "+strTemp3) // testing

          if (currentUserId1.equals(strTemp3) && alId1.equals(strTemp2) /*&& temp == 0*/) {
            currentM = a
            temp = 1
          }
        }
        //sender ! currentUser
        //println("hi "+currentM)
        if (temp == 1) {
          photoArrayBuffer.remove(currentM)
        }
      }
    }
  }

  class AlOperatorFB1 extends Actor {
    override def receive = {
      case sentmyAl => {
        var alList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to photoArrayBuffer.length-1) {

          var strTemp1:String = photoArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(8,strTmpIndex)
          strTmpIndex = strTemp2.indexOf(',')
          var strTemp3:String = strTemp2.substring(strTmpIndex+1,strTemp2.length)
          strTemp2 = strTemp2.substring(0,strTmpIndex)
          strTmpIndex = strTemp3.indexOf(',')
          strTemp3 = strTemp3.substring(0,strTmpIndex)
          //println(strTemp1+" "+strTemp2+" "+strTemp3) // testing

          if (currentUserId1.equals(strTemp3)) {
            var strTempm1:String = strTemp2
            alList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        //alList = alList.reverse
        alList = alList.distinct
        sender ! alList
      }
    }
  }

  class AlPhOperatorFB1 extends Actor {
    override def receive = {
      case sentmyAlPh => {
        var alphList = ArrayBuffer[String]()
        var currentUserId1:String = currentUserId
        for (a <- 0 to photoArrayBuffer.length-1) {

          var strTemp1:String = photoArrayBuffer(a).toString()
          //println(strTemp)
          var strTmpIndex:Int = strTemp1.indexOf(',')
          var strTemp2:String = strTemp1.substring(strTmpIndex+1,strTemp1.length-1)
          strTemp1 = strTemp1.substring(8,strTmpIndex)
          strTmpIndex = strTemp2.indexOf(',')
          var strTemp3:String = strTemp2.substring(strTmpIndex+1,strTemp2.length)
          strTemp2 = strTemp2.substring(0,strTmpIndex)
          //println(strTemp1+" "+strTemp2+" "+strTemp3) // testing

          if (currentUserId1.equals(strTemp3)) {
            var strTempm1:String = ("{"+strTemp2+","+strTemp1+"}")
            alphList += strTempm1
            //println(messageArrayBuffer(a).toString())
          }
          //println(a)
        }
        alphList = alphList.sorted
        sender ! alphList
      }
    }
  }

  // Some syntax needed for the above code to run but are redundant in functionality
  lazy val miningRoute = {
    get {
      path("myFacebook" / "trial") {
        complete{
          (miningActor ? RemainingMiningTime).mapTo[Int]
            .map(s => s"Your amber will be mined in $s")
        }
      }
    }
  }

  class HelloActor extends Actor {
    override def receive = {
      case ctx: RequestContext => ctx.complete("Welcome to Amber Gold")
    }
  }
  class MiningActor extends Actor {
    private val timeRemaining = 10

    override def receive = {
      case RemainingMiningTime => sender ! timeRemaining
    }
  }
  object transform
  object RemainingMiningTime
  object findcurrentUser
  object findcurrentM
  object findcurrentF
  object findcurrentPh
  object findcurrentAl
  object findcurrentPage
  object sentmyM
  object sentmyPM
  object sentmyM2
  object sentmyF
  object sentmyPh
  object sentmyAl
  object sentmyAlPh
  object sentmyPage
  object sentmymKey
  object sentmypKey
  object sentmypmKey
}

