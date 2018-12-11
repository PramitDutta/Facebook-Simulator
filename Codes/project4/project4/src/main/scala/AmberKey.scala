/**
 * Created by csur on 12/18/2015.
 */

import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

import scala.collection.mutable.ArrayBuffer

/**
 * Created by csur on 11/30/2015.
 */

trait facebookkey

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbkey(Id:String, keyStr:String, keyType:String) extends facebookkey
case class fbkeypart(Id:String, keyStr:String) extends facebookkey
//case class fbString(messageId:String, userId:String, messageFeed: String) extends facebookm

object AmberKey {
  val amberskey = ArrayBuffer[facebookkey](
    //fbPage(pageId = "a1", msg = "mess 1"),
    //fbPage(pageId = "a1", msg = "mess 2"),
    //fbPage(pageId = "a1", msg = "mess 3"),
    //fbPage(pageId = "a1", msg = "mess 4"),
    //fbPage(pageId = "a2", msg = "mess 5"),
    fbkey(Id = "a2", keyStr = "mess 6", keyType = "1")
  )

  //val amberlm = List[facebookm]()

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbkey], classOf[fbkeypart])))
  def toJson(amberskey: ArrayBuffer[facebookkey]): String = writePretty(amberskey)
  def toJson(amberkey: facebookkey): String = writePretty(amberkey)
  //def toJson(amberlm: ArrayBuffer[String]): String = writePretty(amberlm)
}