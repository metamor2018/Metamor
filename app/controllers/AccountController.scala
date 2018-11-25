package controllers

import auth.AuthAction
import javax.inject.{ Inject, Singleton }
import models.service.MixInAccountService
import play.api._
import play.api.mvc._
import play.api.libs.circe.Circe

@Singleton
class AccountController @Inject()(cc: ControllerComponents, authAction: AuthAction)
    extends AbstractController(cc)
    with Circe
    with MixInAccountService {

  def signup(): Action[AnyContent] = authAction { implicit request =>
    request.jwt.subject match {
      case None => BadRequest // OpenIdがない場合
      case Some(authId) =>
        if (ifExistsCreate(authId)) Ok
        else BadGateway
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
