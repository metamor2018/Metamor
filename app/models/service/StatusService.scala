package models.service

import models.entity.Status
import models.repository.{ MixInStatusRepository, UsesStatusRepository }
import scalikejdbc.DB

import scala.util.{ Failure, Success, Try }

trait StatusService extends UsesStatusRepository {

  /**
    * 投稿作成
    * @param worldId
    * @param characterId
    * @param reply
    * @param inReplyToId
    * @param text
    * @return 作成した投稿
    */
  def create(worldId: Long,
             characterId: String,
             reply: Boolean,
             inReplyToId: Option[Long],
             text: String): Either[Throwable, Status] =
    DB localTx { implicit session =>
      for {
        statusId <- statusRepository.create(worldId, characterId, reply, inReplyToId, text)
        statusOpt <- statusRepository.find(statusId)
        status <- Try(statusOpt.get)
      } yield status
    } match {
      case Failure(e) => Left(e)
      case Success(s) => Right(s)
    }
}

trait UsesStatusService {
  val statusService: StatusService
}

trait MixInStatusService {
  val statusService: StatusService = new StatusService with MixInStatusRepository
}
