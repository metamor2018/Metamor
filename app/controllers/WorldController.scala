package controllers

import java.time.ZonedDateTime

import auth.AuthAction
import forms.WorldEntryForm
import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInWorldService
import scalaz.Scalaz._
import scalaz._

object WorldController {
  case class WorldForm(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime)
}

@Singleton
class WorldController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInWorldService {

  import WorldController._

  /**
   * クリエイターを作成
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
        println(e)
        BadRequest(("status" -> "ng").asJson)
    }
  }

  def getWorlds() = Action {
    val worlds = worldService.getWorlds()
    Ok((worlds.asJson))
  }

  def entry() = authAction(circe.json[WorldEntryForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) => BadRequest(e.toVector.asJson)
      case Success(a) =>
        worldService.entry(a.characterId, a.worldId)
        Ok(("status" -> "ok").asJson)
    }
  }
}
