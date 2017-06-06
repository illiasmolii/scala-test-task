package models

import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.json._

case class Item(name: String,
                message: String,
                timestamp: Option[Date] = Some(new Date()))

object Item {

  val dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'")

  object ItemWrites extends Writes[Item] {
    def writes(item: Item): JsObject = JsObject(
      Seq(
        "name" -> JsString(item.name),
        "message" -> JsString(item.message),
        "timestamp" -> JsString(item.timestamp.map(dateFormat.format).getOrElse(""))
      )
    )
  }

  object ItemReads extends Reads[Item] {
    def reads(json: JsValue) = json match {
      case JsArray(Seq(JsString(name), JsString(message))) =>
        JsSuccess(Item(name, message))
      case json: JsObject => {
        val name = (json \ "name").as[String]
        val message = (json \ "message").as[String]
        JsSuccess(Item(name, message))
      }
    }
  }

  implicit val itemFormat: Format[Item] = Format(ItemReads, ItemWrites)
}
