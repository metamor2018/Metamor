package forms.validations

import forms.validations.WorldValidations.creatorService
import models.service.MixInCreatorService
import scalaz.Scalaz._
import scalaz._

object CreatorValidations extends MixInCreatorService {

  def id(id: Long) = {
    id match {
//      case id if creatorService.existsById(id) => id.successNel[Long]
      case _ => id.successNel[String]
    }
  }

  def displayId(id: String): ValidationNel[String, String] = {
    id match {
      case id if id.length < 4                 => "idが短すぎます".failureNel[String]
      case id if id.length >= 20               => "idが長すぎます".failureNel[String]
      case id if creatorService.existsById(id) => "既に存在するidです".failureNel[String]
      case _                                   => id.successNel[String]
    }
  }

  def name(name: String) = {
    name match {
      case name if name.isEmpty      => "名前を入力してください".failureNel[String]
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

  def exists(creatorId: String): ValidationNel[String, String] = {
    creatorId match {
      case creatorId if !creatorService.existsById(creatorId) =>
        "存在しない創作者です".failureNel[String]
      case _ =>
        creatorId.successNel[String]
    }
  }

}
