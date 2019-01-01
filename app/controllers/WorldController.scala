package controllers

import java.time.ZonedDateTime

import auth.AuthAction
import forms.{ CreatorIdForm, WorldEntryForm }
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInWorldService
import models.service.MixInCharacterService
import scalaz.Scalaz._
import scalaz._

object WorldController {
  case class WorldForm(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime)
}

@Singleton
class WorldController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInWorldService
    with MixInCharacterService {

  import WorldController._

  /**
   * クリエイターを作成
   *
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def create() = Action(circe.json[WorldForm]) { implicit request =>
    val worldForm = request.body
    //ログインしてる程のID
    val creatorId = "7"

    try {
      worldService.create(worldForm.name, creatorId, worldForm.detail, worldForm.startedAt)
      Ok(("status" -> "ok").asJson)
    } catch {
      case e: Exception =>
        BadRequest(("status" -> "ng").asJson)
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
  def entry() = authAction(circe.json[WorldEntryForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(a) =>
        try {
          worldService.entry(a.characterId, a.worldId)
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
  def getByCreatorId(creatorId: String) = authAction(circe.json[CreatorIdForm]) {
    implicit request =>
      request.body.validate() match {
        case Failure(e) =>
          BadRequest(e.toVector.asJson)
        case Success(a) =>
          try {
            val worlds = worldService.getByCreatorId(a)
            Ok(worlds.asJson)
          } catch {
            case e: Exception =>
              BadGateway
          }

      }

  }
}
