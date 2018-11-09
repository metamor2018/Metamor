package controllers

import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.CharacterService

object CharacterController {
  case class CharacterForm(creatorId: String, displayId: String, name: String)
}

@Singleton
class CharacterController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
    with Circe {

  import CharacterController._

  val testCreator = "1"

  /**
   * クリエイターを作成
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def create() = Action(circe.json[CharacterForm]) { implicit request =>
    val CharacterForm = request.body

    try {
      CharacterService.create(testCreator, CharacterForm.displayId, CharacterForm.name)
      Ok(("status" -> "ok").asJson)
    } catch {
      case e: Exception => BadRequest(("status" -> "ng").asJson)
    }
  }
}
