package models.entity
import java.time.ZonedDateTime

case class Character(id: Long,
                     creatorId: Long,
                     displayId: String,
                     name: String,
                     profile: Option[String],
                     icon: Option[String],
                     createdAt: ZonedDateTime,
                     updatedAt: ZonedDateTime)
