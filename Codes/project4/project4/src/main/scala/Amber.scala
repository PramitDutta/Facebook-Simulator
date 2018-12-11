import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import scala.collection.mutable.ArrayBuffer

trait facebook

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbprofile(id:String, fname: String, lname: String, birth: String, locationFrom: String, locationIn: String, token: String) extends facebook
case class fbprofilepart(id:String, fname: String, lname: String, birth: String) extends facebook

object Amber {
  val ambers = ArrayBuffer[facebook](
   //fbprofile(id = "user1", fname = "Pramit", lname = "Dutta", birth = "01-01-2015", locationFrom = "India", locationIn = "US", token = "user92929Password"),
   //fbprofile(id = "user92928", fname = "Chiranjib", lname = "Sur", birth = "01-01-2015", locationFrom = "India", locationIn = "US", token = "user92929Password"),
   //fbprofile(id = "user92927", fname = "Tom", lname = "Haykin", birth = "01-01-2015", locationFrom = "India", locationIn = "US", token = "user92929Password"),
   fbprofile(id = "user92926", fname = "Tim", lname = "Tebow", birth = "01-01-2015", locationFrom = "India", locationIn = "US", token = "user92929Password")
  )

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbprofile], classOf[fbprofilepart])))
  def toJson(ambers: ArrayBuffer[facebook]): String = writePretty(ambers)
  def toJson(amber: facebook): String = writePretty(amber)
  def toJson(elementString: String): String = writePretty(elementString)
}

