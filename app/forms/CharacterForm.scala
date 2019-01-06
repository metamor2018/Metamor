package forms

import forms.validations.{ CharacterValidations, CreatorValidations }
import scalaz.{ NonEmptyList, Validation }
import scalaz.Scalaz._

case class CharacterCreateForm(id: String, creatorId: String, name: String) {
  def validate() = {
    (
      CharacterValidations.exists(this.id) |@|
        CreatorValidations.exists(this.creatorId) |@|
        CharacterValidations.name(this.name)
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

case class CharacterEditForm(id: String, name: String, profile: String, icon: String) {

  def validate(): Validation[NonEmptyList[String], CharacterEditForm] = {
    (
      CharacterValidations.notExists(this.id) |@|
        CharacterValidations.name(this.name) |@|
        CharacterValidations.profile(this.profile) |@|
        CharacterValidations.icon(this.icon)
    )(CharacterEditForm)
  }
}
