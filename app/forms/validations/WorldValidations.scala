package forms.validations

import models.service.{ MixInCharacterService, MixInCreatorService, MixInWorldService }
import scalaz.Scalaz._
import scalaz._

object WorldValidations
    extends MixInWorldService
    with MixInCharacterService
    with MixInCreatorService {

  def isEntryByCharacterId(characterId: String, worldId: Long): ValidationNel[String, String] = {
    characterId match {
      case characterId if worldService.existsEntry(characterId, worldId) =>
        "ワールドに参加済みです".failureNel[String]
      case characterId if !characterService.exists(characterId) =>
        "存在しないキャラクターです".failureNel[String]
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

  def confirmExistenceOfCreatorId(creatorId: String): ValidationNel[String, String] = {
    creatorId match {
      case creatorId if !creatorService.existsById(creatorId) =>
        "存在しない創作者です".failureNel[String]
      case _ =>
        creatorId.successNel[String]
    }
  }

}
