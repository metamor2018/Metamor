package forms
import forms.validations.WorldValidations
import scalaz.Scalaz._

case class WorldEntryForm(characterId: Long, worldId: Long) {

  def validate() = {
    (
      WorldValidations.isEntryByCharacterId(this.characterId, this.worldId) |@|
        WorldValidations.exists(this.worldId)
    )(WorldEntryForm)
  }

}

case class CreatorIdForm(creatorId: Long) {

  def validate() = {
    (
      WorldValidations.confirmExistenceOfCreatorId(this.creatorId)
    )
  }
}
