package forms

case class StatusForm(
    reply: Boolean,
    inReplyToId: Option[String],
    text: String
)
