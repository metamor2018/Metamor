package forms.validations

import models.service.MixInCharacterService
import scalaz.Scalaz._
import scalaz._

object CharacterValidations extends MixInCharacterService {

  def exists(id: String): ValidationNel[String, String] = {
    id match {
      case id if characterService.exists(id) =>
        "既に存在するキャラクターidです".failureNel[String]
      case _ =>
        id.successNel[String]
    }
  }

}
