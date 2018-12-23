package forms

import forms.validations.CharacterValidations
import scalaz.Scalaz._

case class CharacterCreateForm(id: String, creatorId: String) {
  def characterValidation() = {
    (
      CharacterValidations.createExistCharacter(this.id) |@|
        CharacterValidations.createExistCreator(this.creatorId)
    )(CharacterCreateForm)
  }
}
case class CharacterDeleteForm(id: String) {
  def Validation() = {
    (
      CharacterValidations.deleteExist(this.id)
    )
  }
}
