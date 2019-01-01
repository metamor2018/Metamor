package models.entity

import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

object Status {

  def *(rs: WrappedResultSet): Status = {
    Status(
      rs.long("id"),
      rs.long("world_id"),
      rs.string("character_id"),
      rs.boolean("reply"),
      rs.longOpt("in_reply_to_id"),
      rs.string("text"),
      rs.zonedDateTime("created_at"),
      rs.zonedDateTime("updated_at")
    )
  }
}

case class Status(
    id: Long,
    worldId: Long,
    characterId: String,
    reply: Boolean,
    inReplyToId: Option[Long],
    text: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
)
