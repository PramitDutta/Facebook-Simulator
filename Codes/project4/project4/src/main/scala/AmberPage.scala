import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

import scala.collection.mutable.ArrayBuffer

/**
 * Created by csur on 11/30/2015.
 */

trait facebookpage

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbPage(pageId:String, msg:String) extends facebookpage
case class fbPagepart(pageId:String) extends facebookpage
//case class fbString(messageId:String, userId:String, messageFeed: String) extends facebookm

object AmberPage {
  val amberspage = ArrayBuffer[facebookpage](
    //fbPage(pageId = "a1", msg = "mess 1"),
    //fbPage(pageId = "a1", msg = "mess 2"),
    //fbPage(pageId = "a1", msg = "mess 3"),
    //fbPage(pageId = "a1", msg = "mess 4"),
    //fbPage(pageId = "a2", msg = "mess 5"),
    fbPage(pageId = "a2", msg = "mess 6")
  )

  //val amberlm = List[facebookm]()

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbPage], classOf[fbPagepart])))
  def toJson(amberspage: ArrayBuffer[facebookpage]): String = writePretty(amberspage)
  def toJson(amberpage: facebookpage): String = writePretty(amberpage)
  //def toJson(amberlm: ArrayBuffer[String]): String = writePretty(amberlm)
}