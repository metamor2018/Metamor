package models.entity

case class Status(
    id: Long,
    world: World,
    character: Character,
    reply: Boolean,
    text: String
)
