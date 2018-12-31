package controllers

import auth.AuthAction
import forms.StatusForm
import javax.inject.Inject
import models.service.MixInStatusService
import play.api.libs.circe.Circe
import play.api.mvc.{ AbstractController, ControllerComponents }
import io.circe.generic.auto._
import io.circe.syntax._

class StatusController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with MixInStatusService
    with Circe {

  /**
   * 投稿を作成する
   * @param characterId
   * @param worldId
   * @return
   */
  def create(characterId: String, worldId: Long) = authAction(circe.json[StatusForm]) {
    implicit request =>
      val statusForm = request.body
      statusService.create(
        worldId,
        characterId,
        statusForm.reply,
        statusForm.inReplyToId,
        statusForm.text
      ) match {
        case Left(e) =>
          println(e)
          BadGateway
        case Right(s) => Created(s.asJson)
      }
  }
}
