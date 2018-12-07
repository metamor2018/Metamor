package forms.validations

import models.service.MixInWorldService
import scalaz.Scalaz._
import scalaz._

object WorldValidations extends MixInWorldService {

  def WorldEntryCharacterId(characterId: Long, worldId: Long): ValidationNel[String, Long] = {
    characterId match {
      case characterId if worldService.existsEntry(characterId, worldId) =>
        "既に存在するidです".failureNel[Long]
      case _ => characterId.successNel[String]
    }
  }

}
