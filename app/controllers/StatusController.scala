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
  def get(id: Long) = Action { implicit request =>
    (request.getQueryString("statusId") match {
      case None    => statusService.getByWorldId(id)
      case Some(s) => statusService.getByWorldIdOld(id, s.toLong)
    }) match {
      case Left(e)  => BadGateway
      case Right(s) => Ok(s.asJson)
    }
  }

  /**
    * キャラクター別に投稿を取得
    * @param characterId
    * @return List[Status]
    */
  def getByCharacterId(worldId: Long, characterId: String) = Action {
    statusService.getByCharacterId(worldId, characterId) match {
      case Left(e) =>
        println(e)
        BadGateway
      case Right(s) => Ok(s.asJson)
    }
  }

  /**
    * 指定された個所から最新までの投稿を取得
    * @param id
    * @return
    */
  def getToLast(id: Long) = Action {
    statusService.getToLast(id) match {
      case Left(e)  => BadGateway
      case Right(s) => Ok(s.asJson)
    }
  }

}
