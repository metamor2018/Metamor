package forms.validations

import models.service.MixInStatusService
import scalaz.Scalaz._
import scalaz._

object StatusValidations extends MixInStatusService {

  def inReplyToId(inReplyToId: Option[Long], reply: Boolean): ValidationNel[String, Option[Long]] =
    inReplyToId match {
      case _ if !reply                               => None.successNel[String] // replyじゃないなら実施しない
      case idOpt if idOpt.isEmpty                    => "投稿が選択されていません".failureNel[Option[Long]]
      case idOpt if !statusService.exists(idOpt.get) => "存在しない投稿です".failureNel[Option[Long]]
      case _                                         => inReplyToId.successNel[String]
    }

  def text(text: String): ValidationNel[String, String] =
    text match {
      case text if text.isEmpty => "内容がありません".failureNel[String]
      case _                    => text.successNel[String]
    }
}
