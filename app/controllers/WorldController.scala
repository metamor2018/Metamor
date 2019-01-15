package controllers

import auth.AuthAction
import forms.{ WorldEntryForm, WorldForm }
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.{ MixInCharacterService, MixInCreatorService, MixInWorldService }
import scalaz.Scalaz._
import scalaz._

@Singleton
class WorldController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInWorldService
    with MixInCharacterService
    with MixInCreatorService {

  /**
    * ワールドを作成
    *
    * @return 成功 Created
    *         失敗 BadRequest バリデーションエラー
    *             BadGateway ioエラー
    */
  def create() = authAction(circe.json[WorldForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        worldService.create(a.name, a.creatorId, a.detail) match {
          case Left(e)  => BadGateway
          case Right(s) => Created
        }
    }

  }

  def getWorlds() = Action {
    val worlds = worldService.getWorlds()
    Ok((worlds.asJson))
  }

  /**
    * 開催中のワールド一覧の取得
    * @return
    */
//  def getEnable() = Action {
//    val holdWorlds = worldService.getEnable()
//    Ok((holdWorlds.asJson))
//  }

  /**
    * ワールド参加
    *
    * @return
    */
  def entry(worldId: Long, characterId: String) = authAction { implicit request =>
    (worldId, characterId) match {
      case _ if !characterService.exists(characterId)          => NotFound
      case _ if !worldService.exists(worldId)                  => NotFound
      case _ if worldService.existsEntry(characterId, worldId) => new Status(SEE_OTHER)
      case _ =>
        try {
          worldService.entry(characterId, worldId)
          Ok
        } catch {
          case e: Exception =>
            BadGateway
        }
    }
  }

  /**
    * creatorIdからワールド一覧を取得
    * @param creatorId
    * @return
    */
  def getByCreatorId(creatorId: String) = Action { implicit request =>
    if (!creatorService.existsById(creatorId))
      NotFound
    else
      worldService.getByCreatorId(creatorId) match {
        case Left(e)  => BadGateway
        case Right(s) => Ok(s.asJson)
      }
  }

  def find(id: Int) = Action {
    worldService.find(id) match {
      case Left(e) =>
        e match {
          case e: NoSuchElementException => NotFound
          case _                         => BadGateway
        }
      case Right(s) => Ok(s.asJson)
    }
  }
}
