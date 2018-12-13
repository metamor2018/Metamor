package forms.validations

import models.service.{ MixInCharacterService, MixInWorldService }
import scalaz.Scalaz._
import scalaz._

object WorldValidations extends MixInWorldService with MixInCharacterService {

  def isEntryByCharacterId(characterId: Long, worldId: Long): ValidationNel[String, Long] = {
    characterId match {
      case characterId if worldService.existsEntry(characterId, worldId) =>
        "ワールドに参加済みです".failureNel[Long]
      case characterId if !characterService.exists(characterId) =>
        "存在しないキャラクターです".failureNel[Long]
      case _ =>
        characterId.successNel[String]
    }
  }

  def exists(worldId: Long): ValidationNel[String, Long] = {
    worldId match {
      case worldId if !worldService.exists(worldId) =>
        "存在しないワールドです".failureNel[Long]
      case _ =>
        worldId.successNel[String]
    }
  }

}
