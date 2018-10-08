package controllers

import service.ContentService
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object ContentController {
  val contentService = ContentService

  case class ContentForm(userId: String, content: String)

  implicit val contentFormReads = (
    (__ \ "userId").read[String] and
      (__ \ "content").read[String]
  )(ContentForm)
}

@Singleton
class ContentController @Inject()(cc: ControllerComponents,
                                  messagesAction: MessagesActionBuilder)
    extends AbstractController(cc) {

  import ContentController._

  def create() = messagesAction(parse.json) { implicit request =>
    request.body
      .validate[ContentForm]
      .map { form =>
        val id = contentService.create(form.userId, form.content)
        Ok(Json.obj("result" -> "success", "id" -> id))
      }
      .recoverTotal { e =>
        BadRequest(
          Json.obj("result" -> "failure", "error" -> JsError.toJson(e))
        )
      }
  }

}
