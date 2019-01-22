package forms.validations

import models.service.{ MixInCharacterService, MixInCreatorService }
import scalaz.Scalaz._
import scalaz._

object CharacterValidations extends MixInCharacterService with MixInCreatorService {

  def id(id: String): ValidationNel[String, String] = {
    id match {
      case id if id.length < 4               => "idが短すぎます".failureNel[String]
      case id if id.length >= 20             => "idが長すぎます".failureNel[String]
      case id if characterService.exists(id) => "既に存在するidです".failureNel[String]
      case _                                 => id.successNel[String]
    }
  }

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

  def profile(profile: Option[String]): ValidationNel[String, Option[String]] = {
    profile match {
//      case profile if profile.length >= 100 => "プロフィールが長すぎます".failureNel[Option[String]]
      case _ => profile.successNel[String]
    }
  }

  def icon(icon: Option[String]): ValidationNel[String, Option[String]] = {
    icon match {
      case _ => icon.successNel[String]
    }
  }
}
