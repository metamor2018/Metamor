package forms

import forms.validations.{ CreatorValidations, WorldValidations }
import scalaz.Scalaz._
import scalaz.{ NonEmptyList, Validation }

case class WorldForm(name: String, creatorId: String, detail: String) {
  def validate(): Validation[NonEmptyList[String], WorldForm] = {
    (
      WorldValidations.name(this.name) |@|
        CreatorValidations.exists(this.creatorId) |@|
        WorldValidations.detail(this.detail)
    )(WorldForm)
  }
}

case class WorldEntryForm(characterId: String, worldId: Long) {

  def validate() = {
    (
      WorldValidations.isEntryByCharacterId(this.characterId, this.worldId) |@|
        WorldValidations.exists(this.worldId)
    )(WorldEntryForm)
  }
}
