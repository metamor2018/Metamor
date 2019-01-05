package forms

import forms.validations.{ CharacterValidations, CreatorValidations }
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
case class CharacterFetchListForm(creatorId: String) {
  def validate() = {
    (
      CreatorValidations.exists(creatorId)
    )
  }
}
