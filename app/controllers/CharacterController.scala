package controllers

import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInCharacterService

object CharacterController {
  case class CharacterForm(creatorId: String, displayId: String, name: String)
  case class CharacterDeleteForm(Id: Long)
}

@Singleton
class CharacterController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
    with Circe
    with MixInCharacterService {

  import CharacterController._

  /**
   * クリエイターを作成
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def create() = Action(circe.json[CharacterForm]) { implicit request =>
    val CharacterForm = request.body
    //ログインしてる程のID
    val createrId = "7"

    try {
      characterService.create(createrId, CharacterForm.displayId, CharacterForm.name)
      Ok(("status" -> "ok").asJson)
    } catch {
      case e: Exception => BadRequest(("status" -> "ng").asJson)
    }
  }

  def delete() = Action(circe.json[CharacterDeleteForm]) { implicit request =>
    val deleteForm = request.body
    //存在している程のキャラクターのID
    val deleteCharacters = characterService.delete(deleteForm.Id)
    deleteCharacters match {
      case 1 => Ok(("status" -> "ok").asJson)
      case 0 => BadRequest(("status" -> "ng").asJson)
    }

  }
}
