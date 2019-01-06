package controllers

import javax.inject.{ Inject, Singleton }
import auth.AuthAction
import forms.validations.CreatorValidations
import forms.{ CharacterCreateForm, CharacterDeleteForm }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInCharacterService
import scalaz.{ Failure, Success }
import scalaz.Scalaz._
@Singleton
class CharacterController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInCharacterService {

  lazy val creatorValidations = CreatorValidations

  /**
   * キャラクターを作成するためにバリデーションをかけ成功ならcharacterCreatingPartを呼ぶ
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   *         失敗(charaterIdが存在する時) 既に存在するキャラクターidです
   *         失敗(creatorIdが存在しない時) 存在しない創作者です
   */
  def create() = authAction(circe.json[CharacterCreateForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        try {
          beGoingToCreate(a)
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
   *        失敗(idが存在しない時) 存在しないキャラクターidです
   */
  def delete() = authAction(circe.json[CharacterDeleteForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        try {
          characterService.delete(a)
          Ok(("status" -> "ok").asJson)
        } catch {
          case e: Exception =>
            BadGateway(("status" -> "ng").asJson)
        }

    }
  }

  /**
   * キャラクターを作成する
   * @return
   */
  private def beGoingToCreate(characterCreateForm: CharacterCreateForm): Long =
    characterService.create(characterCreateForm.id,
                            characterCreateForm.creatorId,
                            characterCreateForm.name)

  /**
   * 指定した創作者のキャラクター一覧取得
   * @param creatorId
   * @return 成功　指定した創作者のキャラクター一覧
   *         失敗　NotFound 存在しない創作者IDが来た場合
   */
  def getByCreatorId(creatorId: String) = Action { implicit request =>
    creatorValidations.exists(creatorId) match {
      case Failure(e) => NotFound
      case Success(creatorId) =>
        try {
          val characters = characterService.getByCreatorId(creatorId)
          Ok(characters.asJson)
        } catch {
          case e: Exception => BadGateway
        }
    }
  }
}
