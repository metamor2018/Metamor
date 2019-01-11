package models.entity

import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

object Status {

  def *(rs: WrappedResultSet): Status = {
    Status(
      rs.long("id"),
      rs.long("world_id"),
      Character(
        rs.string("character_id"),
        Creator(
          rs.string("creator_id"),
          rs.long("account_id"),
          rs.string("creator_name"),
          rs.stringOpt("creator_profile"),
          rs.stringOpt("creator_icon"),
          rs.boolean("creator_official"),
          rs.zonedDateTimeOpt("creator_deleted_at"),
          rs.zonedDateTime("creator_created_at"),
          rs.zonedDateTime("creator_updated_at")
        ),
        rs.string("character_name"),
        rs.stringOpt("character_profile"),
        rs.stringOpt("character_icon"),
        rs.zonedDateTimeOpt("character_deleted_at"),
        rs.zonedDateTime("character_created_at"),
        rs.zonedDateTime("character_updated_at")
      ),
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
    character: Character,
    reply: Boolean,
    inReplyToId: Option[Long],
    text: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
)
