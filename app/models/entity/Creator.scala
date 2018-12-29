package models.entity

import java.time.ZonedDateTime

case class Creator(
    id: String,
    accountId: Long,
    name: String,
    profile: String,
    icon: String,
    official: Boolean,
    deletedAt: ZonedDateTime,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
)
