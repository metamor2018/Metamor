package models.entity

import java.time.ZonedDateTime

import scalikejdbc.WrappedResultSet

object World {
  def *(rs: WrappedResultSet) =
    World(
      rs.long("id"),
      rs.string("name"),
      rs.string("detail"),
      rs.zonedDateTimeOpt("started_at"),
      rs.zonedDateTimeOpt("ended_at"),
      rs.longOpt("emblem_id"),
      rs.zonedDateTime("created_at"),
      rs.zonedDateTime("updated_at")
    )
}

case class World(id: Long,
                 name: String,
                 detail: String,
                 startedAt: Option[ZonedDateTime],
                 endedAt: Option[ZonedDateTime],
                 emblemId: Option[Long],
                 createdAt: ZonedDateTime,
                 updatedAt: ZonedDateTime)

case class Entry(id: Long,
                 characterId: Long,
                 worldId: Long,
                 createdAt: ZonedDateTime,
                 updatedAt: ZonedDateTime)
