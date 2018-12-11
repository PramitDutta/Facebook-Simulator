import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

import scala.collection.mutable.ArrayBuffer

trait facebookf

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbFriend(friendId:String, user1Id:String, user2Id: String) extends facebookf
case class fbFriendpart(user1Id:String, user2Id: String) extends facebookf

object AmberF {
  val ambersf = ArrayBuffer[facebookf](
    //fbFriend(friendId = "f1", user1Id = "user1", user2Id = "user2"),
    //fbFriend(friendId = "f1", user1Id = "user1", user2Id = "user3"),
    //fbFriend(friendId = "f1", user1Id = "user2", user2Id = "user8"),
    fbFriend(friendId = "f1", user1Id = "user7582", user2Id = "user8787757")
  )

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbFriend], classOf[fbFriendpart])))
  def toJson(ambersf: ArrayBuffer[facebookf]): String = writePretty(ambersf)
  def toJson(amberf: facebookf): String = writePretty(amberf)
  //def toJson(elementStringm: String): String = writePretty(elementStringm)
}