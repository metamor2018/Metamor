package forms
import forms.validations.{ CreatorValidations, WorldValidations }
import scalaz.Scalaz._

case class WorldEntryForm(characterId: String, worldId: Long) {

  def validate() = {
    (
      WorldValidations.isEntryByCharacterId(this.characterId, this.worldId) |@|
        WorldValidations.exists(this.worldId)
    )(WorldEntryForm)
  }

}

case class CreatorIdForm(creatorId: String) {

  def validate() = {
    (
      CreatorValidations.exists(this.creatorId)
    )
  }
}
