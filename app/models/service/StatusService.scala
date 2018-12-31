package models.service

import models.repository.{ MixInStatusRepository, UsesStatusRepository }
import scalikejdbc.DB

import scala.util.{ Failure, Success }

trait StatusService extends UsesStatusRepository {

  def create(worldId: Long,
             characterId: String,
             reply: Boolean,
             inReplyToId: Option[String],
             text: String): Either[Throwable, Long] =
    DB localTx { implicit session =>
      for (statusId <- statusRepository.create(worldId, characterId, reply, inReplyToId, text))
        yield statusId
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
