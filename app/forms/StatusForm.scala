package forms

import forms.validations.StatusValidations
import scalaz.Scalaz._
import scalaz._

case class StatusForm(
    reply: Boolean,
    inReplyToId: Option[Long],
    text: String
) {
  def validate(): ValidationNel[String, StatusForm] =
    (reply.successNel[String] |@|
      StatusValidations.inReplyToId(this.inReplyToId, this.reply) |@|
      StatusValidations.text(this.text))(StatusForm)

}
