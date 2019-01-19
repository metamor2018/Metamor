package controllers

import auth.AuthAction
import javax.inject.{ Inject, Singleton }
import models.service.{ MixInAccountService, MixInCreatorService }
import play.api.mvc._
import play.api.libs.circe.Circe
import io.circe.syntax._

@Singleton
class AccountController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInAccountService
    with MixInCreatorService {

  def signup(): Action[AnyContent] = authAction { implicit request =>
    val authId = request.jwt.subject.get
    accountService.create(authId) match {
      case Left(e) => BadGateway
      case Right(_) =>
        creatorService.existsByAuthId(authId) match { // 創作者が作成されているか確認
          case true  => Ok(Map("existsCreator" -> true).asJson)
          case false => Ok(Map("existsCreator" -> false).asJson)
        }
    }
  }
}
