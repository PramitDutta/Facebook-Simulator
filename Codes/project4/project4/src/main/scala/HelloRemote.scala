/**
 * Created by csur on 12/16/2015.
 */

import java.security.PublicKey

import akka.actor._

case object Start
case class Message(msg: String)
case class SentPublicKey(msg: PublicKey)

object HelloRemote extends App  {
  val system = ActorSystem("HelloRemoteSystem")
  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
  remoteActor ! Message("The RemoteActor is alive")
}

class RemoteActor extends Actor {
  def receive = {
    case Message(msg) =>
      println(s"RemoteActor received message '$msg'")
      sender ! Message("Hello from the RemoteActor")
    case SentPublicKey(key: PublicKey) =>
      test.UserSentPublicKey = key
      sender ! SentPublicKey(test.serverPublicKey)
      println("Server Received Private Key")
    case _ =>
      println("RemoteActor got something unexpected.")

  }
}
