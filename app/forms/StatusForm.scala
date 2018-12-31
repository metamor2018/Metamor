package forms

case class StatusForm(
    reply: Boolean,
    inReplyToId: Option[Long],
    text: String
)
