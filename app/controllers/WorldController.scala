package controllers

import java.time.ZonedDateTime

import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.WorldService

object WorldController {
  case class WorldForm(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime)
}

@Singleton
class WorldController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
    with Circe {

  import WorldController._

  /**
   * クリエイターを作成
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def create() = Action(circe.json[WorldForm]) { implicit request =>
    val worldForm = request.body
    //ログインしてる程のID
    val testCreator = "7"

    try {
      WorldService.create(worldForm.name, testCreator, worldForm.detail, worldForm.startedAt)
      Ok(("status" -> "ok").asJson)
    } catch {
      case e: Exception =>
        println(e)
        BadRequest(("status" -> "ng").asJson)
    }
  }
}

// 2018-04-01T00:00:00.000+09:00[Asia/Tokyo]
