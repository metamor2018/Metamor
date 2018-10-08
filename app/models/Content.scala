package models

import java.time.LocalDateTime

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._

case class Content(id: Int,
                   userId: String,
                   content: String,
                   createdAt: LocalDateTime,
                   updatedAt: LocalDateTime)

object Content extends {

  val * = (rs: WrappedResultSet) =>
    Content(
      rs.int("id"),
      rs.string("user_id"),
      rs.string("content"),
      rs.localDateTime("created_at"),
      rs.localDateTime("updated_at")
  )

  val writes: Writes[Content] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "userId").write[String] and
      (JsPath \ "content").write[String] and
      (JsPath \ "createdAt").write[LocalDateTime] and
      (JsPath \ "updatedAt").write[LocalDateTime]
  )(unlift(Content.unapply))

}
