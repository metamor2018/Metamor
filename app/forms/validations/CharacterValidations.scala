package forms.validations

import models.service.{ MixInCharacterService, MixInCreatorService }
import scalaz.Scalaz._
import scalaz._

object CharacterValidations extends MixInCharacterService with MixInCreatorService {

  def exists(id: String): ValidationNel[String, String] = {
    id match {
      case id if characterService.exists(id) =>
        "既に存在するキャラクターです".failureNel[String]
      case _ =>
        id.successNel[String]
    }
  }

  def notExists(id: String): ValidationNel[String, String] = {
    id match {
      case id if !characterService.exists(id) =>
        "存在しないキャラクターです".failureNel[String]
      case _ =>
        id.successNel[String]
    }
  }

  def name(name: String) = {
    name match {
      case name if name.isEmpty      => "名前が短すぎます".failureNel[String]
      case name if name.length >= 30 => "名前が長すぎます".failureNel[String]
      case _                         => name.successNel[String]

    }
  }
}
