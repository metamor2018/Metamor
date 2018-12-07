package forms
import forms.validations.{ CreatorValidations, WorldValidations }
import scalaz.Scalaz._
import scalaz._

case class WorldEntryForm(characterId: Long, worldId: Long) {

  def validate() = {
    (
      WorldValidations.WorldEntryCharacterId(this.characterId, this.worldId) |@|
        worldId.successNel[String]
    )(WorldEntryForm)
  }

}
