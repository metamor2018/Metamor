package forms

import forms.validations.{ CharacterValidations, CreatorValidations }
import scalaz.Scalaz._

case class CharacterCreateForm(id: String,
                               creatorId: String,
                               name: String,
                               profile: Option[String],
                               icon: Option[String]) {
  def validate() = {
    (
      CharacterValidations.id(this.id) |@|
        CreatorValidations.exists(this.creatorId) |@|
        CharacterValidations.name(this.name) |@|
        CharacterValidations.profile(this.profile) |@|
        CharacterValidations.icon(this.icon)
    )(CharacterCreateForm)
  }
}
case class CharacterDeleteForm(id: String) {
  def validate() = {
    (
      CharacterValidations.notExists(this.id)
    )
  }
}
