package models.entity

import java.time.ZonedDateTime

case class World(id: Long,
                 name: String,
                 creatorId: Long,
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
