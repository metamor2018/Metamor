package controllers

import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.CreatorService

object CreatorController {
  case class CreatorForm(displayId: String, name: String)
}

@Singleton
class CreatorController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
    with Circe {

  import CreatorController._

  /**
   * クリエイターを作成
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def create() = Action(circe.json[CreatorForm]) { implicit request =>
    val creatorForm = request.body

    try {
      CreatorService.create(creatorForm.displayId, creatorForm.name)
      Ok(("status" -> "ok").asJson)
    } catch {
      case e: Exception => BadRequest(("status" -> "ng").asJson)
    }
  }

}
