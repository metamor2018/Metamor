package forms.validations

import models.service.{ MixInCharacterService, MixInCreatorService, MixInWorldService }
import scalaz.Scalaz._
import scalaz._

object WorldValidations
    extends MixInWorldService
    with MixInCharacterService
    with MixInCreatorService {

  def name(name: String) = {
    name match {
      case name if name.isEmpty      => "名前を入力してください".failureNel[String]
      case name if name.length > 30 => "名前が長すぎます".failureNel[String]
      case _                         => name.successNel[String]
    }
  }

  def detail(detail: String) = {
    detail match {
      case detail if detail.length > 255 => "詳細が長すぎます".failureNel[String]
      case _                              => detail.successNel[String]
    }
  }

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

}
