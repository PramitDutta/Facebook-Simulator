import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import scala.collection.mutable.ArrayBuffer

trait facebookm

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbFeed(messageId:String, userId:String, messageFeed: String, location: String) extends facebookm
case class fbFeedpart(messageId:String, userId:String, messageFeed: String) extends facebookm
//case class fbString(messageId:String, userId:String, messageFeed: String) extends facebookm

object AmberM {
  val ambersm = ArrayBuffer[facebookm](
    //fbFeed(messageId = "m1", userId = "user3", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeed(messageId = "m2", userId = "user1", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeed(messageId = "m3", userId = "user2", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeed(messageId = "m4", userId = "user1", messageFeed = "My Message 1", location = "Gainesville"),
    //fbFeed(messageId = "m5", userId = "user2", messageFeed = "My Message 1", location = "Gainesville"),
    fbFeed(messageId = "m6", userId = "user1012", messageFeed = "My Message 2", location = "Gainesville")
  )

  //val amberlm = List[facebookm]()

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbFeed], classOf[fbFeedpart])))
  def toJson(ambersm: ArrayBuffer[facebookm]): String = writePretty(ambersm)
  def toJson(amberm: facebookm): String = writePretty(amberm)
  //def toJson(amberlm: ArrayBuffer[String]): String = writePretty(amberlm)
}