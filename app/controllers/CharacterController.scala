package controllers

import auth.AuthAction
import forms.{ CharacterEditForm, CharacterForm }
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInCharacterService
import scalaz.Scalaz._
import scalaz._

object CharacterController {
  case class CharacterForms(creatorId: String, displayId: String, name: String)
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
  def create() = Action(circe.json[CharacterForms]) { implicit request =>
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

  /**
   * キャラクターを削除
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def delete() = Action(circe.json[CharacterDeleteForm]) { implicit request =>
    val deleteForm = request.body
    //存在している程のキャラクターのID
    val deleteCharacters = characterService.delete(deleteForm.Id)
    deleteCharacters match {
      case 1 => Ok(("status" -> "ok").asJson)
      case 0 => BadRequest(("status" -> "ng").asJson)
    }
  }

  def edit(): Action[CharacterEditForm] = authAction(circe.json[CharacterEditForm]) {
    implicit request =>
      characterService.edit(
        request.body.id,
        request.body.displayId,
        request.body.name,
        request.body.profile,
        request.body.icon
      )
      Ok(("status" -> "ok").asJson)
  }
}
