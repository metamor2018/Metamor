package models.entity
import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

object Character {

  def *(rs: WrappedResultSet): Character =
    Character(
      rs.string("id"),
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
      rs.string("name"),
      rs.stringOpt("profile"),
      rs.stringOpt("icon"),
      rs.zonedDateTimeOpt("deleted_at"),
      rs.zonedDateTime("created_at"),
      rs.zonedDateTime("updated_at")
    )

}

case class Character(id: String,
                     creator: Creator,
                     name: String,
                     profile: Option[String],
                     icon: Option[String],
                     deletedAt: Option[ZonedDateTime],
                     createdAt: ZonedDateTime,
                     updatedAt: ZonedDateTime)
