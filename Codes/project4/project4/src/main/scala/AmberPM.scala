/**
 * Created by csur on 12/16/2015.
 */

import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import scala.collection.mutable.ArrayBuffer

trait facebookpm

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbFeedpm(messageId:String, userId:String, messageFeed: String, location: String) extends facebookpm
case class fbFeedpmpart(messageId:String, userId:String, messageFeed: String) extends facebookpm
//case class fbString(messageId:String, userId:String, messageFeed: String) extends facebookm

object AmberPM {
  val amberspm = ArrayBuffer[facebookpm](
    //fbFeedpm(messageId = "m1", userId = "user1", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeedpm(messageId = "m2", userId = "user1", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeedpm(messageId = "m3", userId = "user2", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeedpm(messageId = "m4", userId = "user1", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeedpm(messageId = "m5", userId = "user111", messageFeed = "My Message 1", location = "Gainesville"),
    fbFeedpm(messageId = "m6", userId = "user111", messageFeed = "My Message 2", location = "Gainesville")
  )

  //val amberlm = List[facebookm]()

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbFeedpm], classOf[fbFeedpmpart])))
  def toJson(amberspm: ArrayBuffer[facebookpm]): String = writePretty(amberspm)
  def toJson(amberpm: facebookpm): String = writePretty(amberpm)
  //def toJson(amberlm: ArrayBuffer[String]): String = writePretty(amberlm)
}