package forms

import forms.validations.CharacterValidations
import scalaz.Scalaz._
import scalaz._

case class CharacterForm(displayId: String, name: String) {

  def validate(): Validation[NonEmptyList[String], CharacterForm] = {
    (
      CharacterValidations.displayId(this.displayId) |@|
        CharacterValidations.name(this.name)
    )(CharacterForm)
  }

}

case class CharacterEditForm(id: Long,
                             displayId: String,
                             name: String,
                             profile: String,
                             icon: String) {

  def validate(): Validation[NonEmptyList[String], CharacterEditForm] = {
    (
      CharacterValidations.id(this.id) |@|
        CharacterValidations.displayId(this.displayId) |@|
        CharacterValidations.name(this.name) |@|
        CharacterValidations.profile(this.profile) |@|
        CharacterValidations.icon(this.icon)
    )(CharacterEditForm)
  }
}
