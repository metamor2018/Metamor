package forms

import forms.validations.CharacterValidations

case class CharacterForm(id: String, creatorId: String, name: String) {
  def createValidation() = {
    (
      CharacterValidations.exists(this.id)
    )
  }

  def deleteValidation() = {
    (
      CharacterValidations.exists(this.id)
    )
  }
}
