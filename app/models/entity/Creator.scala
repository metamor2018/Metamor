package models.entity

import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

object Creator {

  def *(rs: WrappedResultSet): Creator = {
    Creator(
      rs.string("id"),
      rs.long("account_id"),
      rs.string("name"),
      rs.stringOpt("profile"),
      rs.stringOpt("icon"),
      rs.boolean("official"),
      rs.zonedDateTimeOpt("deleted_at"),
      rs.zonedDateTime("created_at"),
      rs.zonedDateTime("updated_at")
    )
  }
}

case class Creator(
    id: String,
    accountId: Long,
    name: String,
    profile: Option[String],
    icon: Option[String],
    official: Boolean,
    deletedAt: Option[ZonedDateTime],
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
)
