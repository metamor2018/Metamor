package forms
import java.time.ZonedDateTime

import forms.validations.WorldValidations
import scalaz.Scalaz._

case class WorldForm(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime)

case class WorldEntryForm(characterId: String, worldId: Long) {

  def validate() = {
    (
      WorldValidations.isEntryByCharacterId(this.characterId, this.worldId) |@|
        WorldValidations.exists(this.worldId)
    )(WorldEntryForm)
  }
}
