package controllers

import auth.AuthAction
import forms.CreatorForm
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import models.service.MixInCreatorService
import scalaz.Scalaz._
import scalaz._

@Singleton
class CreatorController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInCreatorService {

  /**
   * クリエイターを作成
   * @return 成功 { status : ok }
   *         失敗 { status : ng }
   */
  def create(): Action[CreatorForm] = authAction(circe.json[CreatorForm]) { implicit request =>
    request.body.validate() match {
      case Failure(e) =>
        BadRequest(e.toVector.asJson)
      case Success(s) =>
        creatorService.create(s.displayId, s.name)
        Ok(("status" -> "ok").asJson)
    }
  }

}
