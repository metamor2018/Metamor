package controllers

import javax.inject.{ Inject, Singleton }
import auth.AuthAction
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInCharacterService
import scalaz.{ Failure, Success }

object CharacterController {
  case class CharacterForm(creatorId: String, displayId: String, name: String)
  case class CharacterDeleteForm(Id: Long)
}

@Singleton
class CharacterController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInCharacterService {

  import CharacterController._

  /**
   * キャラクターを作成
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  /**
   *
   * @return
   */
  def create() = authAction(circe.json[forms.CharacterForm]) { implicit request =>
    request.body.createValidation() match {
      case Failure(e) =>
        BadRequest(e.head.asJson)
      case Success(a) =>
        try {
          characterService.create(request.body.id, request.body.creatorId, request.body.name)
          Ok(("status" -> "ok").asJson)
        } catch {
          case e: Exception =>
            BadGateway(("status" -> "ng").asJson)
        }
    }
  }

  /**
   * キャラクターを削除
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def delete() = authAction(circe.json[CharacterDeleteForm]) { implicit request =>
    val deleteForm = request.body
    //存在している程のキャラクターのID
    val deleteCharacters = characterService.delete(deleteForm.Id)
    deleteCharacters match {
      case 1 => Ok(("status" -> "ok").asJson)
      case 0 => BadRequest(("status" -> "ng").asJson)
    }
  }
}
