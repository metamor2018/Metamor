package controllers

import java.time.ZonedDateTime
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInWorldService

object WorldController {
  case class WorldForm(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime)
  case class CreatorIdForm(creatorId: Long)
}

@Singleton
class WorldController @Inject()(cc: ControllerComponents)
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

  def getByCreatorId(displayId: String) = Action(circe.json[CreatorIdForm]) { implicit request =>
    val creatorIdForm = request.body
    val worlds = worldService.getByCreatorId(creatorIdForm.creatorId)
    Ok((worlds.asJson))
  }
}
