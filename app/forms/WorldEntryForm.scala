package forms
import forms.validations.{ CreatorValidations, WorldValidations }
import scalaz.Scalaz._
import scalaz._

case class WorldEntryForm(characterId: Long, worldId: Long) {

  def validate() = {
    (
      WorldValidations.isEntryByCharacterId(this.characterId, this.worldId) |@|
        WorldValidations.exists(this.worldId)
    )(WorldEntryForm)
  }

}
