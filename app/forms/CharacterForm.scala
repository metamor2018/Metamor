package forms

import forms.validations.{ CharacterValidations, CreatorValidations }
import scalaz.Scalaz._

case class CharacterCreateForm(id: String, creatorId: String) {
  def validate() = {
    (
      CharacterValidations.exists(this.id) |@|
        CreatorValidations.exists(this.creatorId)
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
