package controllers

import javax.inject.{ Inject, Singleton }
import auth.AuthAction
import controllers.CharacterController.CharacterCreateForm
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInCharacterService
import scalaz.{ Failure, Success }
import scalaz.Scalaz._

object CharacterController {
  case class CharacterCreateForm(id: String, creatorId: String, name: String)
}

@Singleton
class CharacterController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInCharacterService {

  /**
   * キャラクターを作成するためにバリデーションをかけ成功ならcharacterCreatingPartを呼ぶ
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   *         失敗(charaterIdが存在する時) 既に存在するキャラクターidです
   *         失敗(creatorIdが存在しない時) 存在しない創作者です
   */
  def create() = authAction(circe.json[forms.CharacterCreateForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        try {
          beGoingToCreate
          Ok(("status" -> "ok").asJson)
        } catch {
          case e: Exception =>
            BadRequest(("status" -> "ng").asJson)
        }
    }
  }

  /**
   * キャラクターを削除
   * @return 成功 { status : ok }
   *        失敗(idが存在しない時) 存在しないキャラクターidです
   */
  def delete() = authAction(circe.json[forms.CharacterDeleteForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        try {
          characterService.delete(a)
          Ok(("status" -> "ok").asJson)
        } catch {
          case e: Exception =>
            BadRequest(("status" -> "ng").asJson)
        }

    }
  }

  /**
   * キャラクターを作成する
   * @return
   */
  private def beGoingToCreate() = Action(circe.json[CharacterCreateForm]) { implicit request =>
    val characterCreateForm = request.body
    characterService.create(characterCreateForm.id,
                            characterCreateForm.creatorId,
                            characterCreateForm.name)
    Ok
  }
}
