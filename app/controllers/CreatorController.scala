package controllers

import auth.AuthAction
import forms.{ CreatorEditForm, CreatorForm }
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
   * 創作者を作成
   * @return 成功 { creatorId : [作成した創作者のid] }
   *         失敗 SeeOther 既に創作者を作成している場合
   *            BadRequest バリデーションエラー
   *            BadGateway 保存に失敗した場合
   */
  def create(): Action[CreatorForm] = authAction(circe.json[CreatorForm]) { implicit request =>
    val creatorForm = request.body
    val authId = request.jwt.subject.get

    // 既に創作者を作成済みの場合
    if (creatorService.existsByAuthId(authId)) {
      new Status(SEE_OTHER)
    } else {
      creatorForm.validate() match {
        case Failure(e) =>
          BadRequest(e.toVector.asJson)
        case Success(s) =>
          creatorService.create(s.id, s.name, authId) match {
            case Left(e) =>
              BadGateway
            case Right(_) =>
              Created(Map("creatorId" -> s.id).asJson)
          }
      }

    }
  }

  def edit(): Action[CreatorEditForm] = authAction(circe.json[CreatorEditForm]) {
    implicit request =>
      creatorService.edit(
        request.body.id,
        request.body.displayId,
        request.body.name,
        request.body.profile,
        request.body.icon
      )
      Ok(("status" -> "ok").asJson)
  }

  /**
   * クリエイターが存在するか確認
   * @return 存在すればtrue
   */
  def exists() = authAction { implicit request =>
    request.jwt.subject match {
      case None => BadRequest // OpenIdがない場合
      case Some(authId) =>
        if (creatorService.existsByAuthId(authId)) Ok(("exists" -> true).asJson)
        else Ok(("exists" -> false).asJson)
    }
  }

}
