/**
 * Created by csur on 12/16/2015.
 */

import java.security.{PublicKey, PrivateKey}

import akka.actor._

case object Start
case class Message(msg: String)
case class SentPublicKey(key: PublicKey)

object Local extends App {

  implicit val system = ActorSystem("LocalSystem")
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")  // the local actor
  localActor ! Start                                                       // start the action

}

class LocalActor extends Actor {

  // create the remote actor
  val remote = context.actorSelection("http://localhost:8080/user/RemoteActor")
  var counter = 0

  def receive = {
    case Start =>
      remote ! Message("Hello from the LocalActor")
      remote ! SentPublicKey(ClientComplete.userPuKey.get("user1"))
    case Message(msg) =>
      println(s"LocalActor received message: '$msg'")
      if (counter < 5) {
        sender ! Message("Hello back to you")
        counter += 1
      }
    case SentPublicKey(key: PublicKey) =>
      ClientComplete.serverPuKey = key
      println("Server Received Private Key")
    case _ =>
      println("LocalActor got something unexpected.")
  }

}