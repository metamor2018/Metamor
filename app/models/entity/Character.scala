package models.entity
import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

object Character {

  def *(rs: WrappedResultSet): Character =
    Character(
      rs.string("id"),
      rs.string("creator_id"),
      rs.string("name"),
      rs.stringOpt("profile"),
      rs.stringOpt("icon"),
      rs.zonedDateTime("created_at"),
      rs.zonedDateTime("updated_at")
    )

}

case class Character(id: String,
                     creatorId: String,
                     name: String,
                     profile: Option[String],
                     icon: Option[String],
                     createdAt: ZonedDateTime,
                     updatedAt: ZonedDateTime)
