package forms.validations

import models.service.MixInCreatorService
import scalaz.Scalaz._
import scalaz._

object CreatorValidations extends MixInCreatorService {

  def displayId(displayId: String): ValidationNel[String, String] = {
    displayId match {
      case id if id.length < 4                        => "idが短すぎます".failureNel[String]
      case id if id.length >= 20                      => "idが長すぎます".failureNel[String]
      case id if creatorService.existsByDisplayId(id) => "既に存在するidです".failureNel[String]
      case _                                          => displayId.successNel[String]
    }
  }

  def name(name: String) = {
    name match {
      case name if name.isEmpty      => "名前を入力してください".failureNel[String]
      case name if name.length >= 30 => "名前が長すぎます".failureNel[String]
      case _                         => name.successNel[String]
    }
  }
}
