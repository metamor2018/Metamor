package models.entity

import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

case class Account(
    id: Long,
    authId: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
)

object Account {

  def *(rs: WrappedResultSet): Account = {
    Account(
      rs.long("id"),
      rs.string("auth_id"),
      rs.zonedDateTime("created_at"),
      rs.zonedDateTime("updated_at")
    )
  }
}
