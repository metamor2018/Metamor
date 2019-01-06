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

  def id(id: String) = {
    id match {
      case _ => id.successNel[String]
    }
  }

  def name(name: String) = {
    name match {
      case name if name.isEmpty      => "名前が短すぎます".failureNel[String]
      case name if name.length >= 30 => "名前が長すぎます".failureNel[String]
      case _                         => name.successNel[String]

    }
  }

  def profile(profile: String) = {
    profile match {
      case profile if profile.length >= 255 => "プロフィールが長すぎます".failureNel[String]
      case _                                => profile.successNel[String]
    }
  }

  def icon(icon: String) = {
    icon match {
      case _ => icon.successNel[String]
    }
  }
}
