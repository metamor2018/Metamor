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
   *         失敗(charaterIdが存在時) 既に存在するキャラクターidです
   *         失敗(creatorIdが存在しない時) 存在しない創作者です
   */
  def create() = authAction(circe.json[forms.CharacterCreateForm]) { implicit request =>
    request.body.characterValidation() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        characterCreatingPart
        Ok(("status" -> "ok").asJson)
    }
  }

  /**
   * キャラクターを作成する
   * @return
   */
  def characterCreatingPart() = authAction(circe.json[CharacterCreateForm]) { implicit request =>
    characterService.create(request.body.id, request.body.creatorId, request.body.name)
    Ok
  }

  /**
   * キャラクターを削除
   * @return 成功 { status : ok }
   *        失敗(idが存在しない時) 存在しないキャラクターidです
   */
  def delete() = authAction(circe.json[forms.CharacterDeleteForm]) { implicit request =>
    request.body.Validation() match {
      case Failure(e) =>
        BadRequest(e.head.asJson)
      case Success(a) =>
        characterService.delete(request.body.id)
        Ok(("status" -> "ok").asJson)

    }
  }
}
