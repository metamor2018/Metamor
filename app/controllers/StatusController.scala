package controllers

import auth.AuthAction
import forms.StatusForm
import javax.inject.Inject
import models.service.{ MixInCharacterService, MixInStatusService, MixInWorldService }
import play.api.libs.circe.Circe
import play.api.mvc.{ AbstractController, ControllerComponents }
import io.circe.generic.auto._
import io.circe.syntax._
import scalaz.Scalaz._
import scalaz._

class StatusController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with MixInStatusService
    with MixInCharacterService
    with MixInWorldService
    with Circe {

  /**
    * 投稿を作成する
    * @param characterId
    * @param worldId
    * @return
    */
  def create(characterId: String, worldId: Long) = authAction(circe.json[StatusForm]) {
    implicit request =>
      if (!characterService.exists(characterId)
          || !worldService.exists(worldId)
          || !worldService.existsEntry(characterId, worldId)) {
        NotFound
      } else {
        request.body.validate() match {
          case Failure(e) =>
            BadRequest(e.toVector.asJson)
          case Success(s) =>
            statusService.create(
              worldId,
              characterId,
              s.reply,
              s.inReplyToId,
              s.text
            ) match {
              case Left(e)  => BadGateway
              case Right(s) => Created(s.asJson)
            }
        }
      }
  }

  /**
    * 投稿を複数取得する
    * @return List[Status]
    */
  def get(characterId: String, worldId: Long) = Action {
    statusService.getByWorldId(worldId) match {
      case Left(e)  => BadGateway
      case Right(s) => Ok(s.asJson)
    }
  }

  /**
    * キャラクター別に投稿を取得
    * @param characterId
    * @return List[Status]
    */
  def getByCharacterId(characterId: String) = Action {
    statusService.getByCharacterId(characterId) match {
      case Left(e)  => BadGateway
      case Right(s) => Ok(s.asJson)
    }
  }

}
