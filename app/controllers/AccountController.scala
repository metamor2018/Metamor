package controllers

import auth.AuthAction
import javax.inject.{ Inject, Singleton }
import models.service.{ MixInAccountService, MixInCreatorService }
import play.api._
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
    request.jwt.subject match {
      case None => BadRequest // OpenIdがない場合
      case Some(authId) =>
        if (ifExistsCreate(authId)) {
          if (creatorService.existsByAuthId(authId))
            Ok(Map("existsCreator" -> true).asJson)
          else
            Ok(Map("existsCreator" -> false).asJson)
        } else BadGateway
    }
  }

  /**
   * authIdがDBに存在すれば何もしない、
   * 存在しなければDBに保存する
   *
   * @param authId
   * @return
   */
  private def ifExistsCreate(authId: String): Boolean =
    if (accountService.exists(authId)) true
    else {
      try {
        accountService.create(authId)
        true
      } catch {
        case e: Exception => false
      }
    }

}
