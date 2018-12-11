import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

import scala.collection.mutable.ArrayBuffer

trait facebookph

//case class BalticAmber(size: Int) extends facebook
//case class MinedAmber(country: String, size: Int) extends facebook

case class fbPhoto(photoId:String, albumId:String, userId: String, photoReal:String) extends facebookph
case class fbPhotopart(photoId:String, userId: String) extends facebookph

object AmberPh {
  val ambersph = ArrayBuffer[facebookph](
    //fbPhoto(photoId = "p1", albumId = "a1", userId = "users1", photoReal = "This is photo 1"),
    //fbPhoto(photoId = "p2", albumId = "a1", userId = "users1", photoReal = "This is photo 2"),
    //fbPhoto(photoId = "p3", albumId = "a11", userId = "users1", photoReal = "This is photo 3"),
    //fbPhoto(photoId = "p4", albumId = "a11", userId = "users1", photoReal = "This is photo 4"),
    //fbPhoto(photoId = "p2", albumId = "a2", userId = "user2", photoReal = "This is photo 5"),
    //fbPhoto(photoId = "p1", albumId = "a1", userId = "user3", photoReal = "This is photo 6"),
    fbPhoto(photoId = "p34", albumId = "a2", userId = "user114", photoReal = "This is photo 7")
  )

  private implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[fbPhoto], classOf[fbPhotopart])))
  def toJson(ambersph: ArrayBuffer[facebookph]): String = writePretty(ambersph)
  def toJson(amberph: facebookph): String = writePretty(amberph)
  //def toJson(elementStringm: String): String = writePretty(elementStringm)
}
