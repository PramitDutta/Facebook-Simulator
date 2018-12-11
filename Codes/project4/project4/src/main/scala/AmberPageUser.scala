import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

import scala.collection.mutable.ArrayBuffer

/**
 * Created by csur on 11/30/2015.
 */

trait facebookpageuser

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbPageUser(pageId:String, userId:String) extends facebookpageuser
case class fbPageUserpart(pageId:String) extends facebookpageuser
//case class fbString(messageId:String, userId:String, messageFeed: String) extends facebookm

object AmberPageUser {
  val amberspageuser = ArrayBuffer[facebookpageuser](
    //fbPageUser(pageId = "a1", userId = "user1"),
    //fbPageUser(pageId = "a1", userId = "user2"),
    fbPageUser(pageId = "a2", userId = "user45462")
  )

  //val amberlm = List[facebookm]()

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbPageUser], classOf[fbPageUserpart])))
  def toJson(amberspageuser: ArrayBuffer[facebookpageuser]): String = writePretty(amberspageuser)
  def toJson(amberpageuser: facebookpageuser): String = writePretty(amberpageuser)
  //def toJson(amberlm: ArrayBuffer[String]): String = writePretty(amberlm)
}