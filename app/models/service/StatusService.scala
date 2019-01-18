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
      case Failure(e) =>
        Left(e)
      case Success(s) => Right(s)
    }

  /**
    * 存在するか確認
    * @param id
    * @return
    */
  def exists(id: Long): Boolean =
    DB readOnly { implicit session =>
      statusRepository.exists(id).get
    }

  /**
    * 最新の投稿を20件取得
    * @return
    */
  def getByWorldId(worldId: Long): Either[Throwable, List[Status]] =
    DB readOnly { implicit session =>
      statusRepository.getByWorldId(worldId) match {
        case Failure(e) => Left(e)
        case Success(s) => Right(s)
      }
    }

  /**
    * 指定された投稿より古い投稿を20件取得
    * @return
    */
  def getByWorldIdOld(worldId: Long, statusId: Long): Either[Throwable, List[Status]] =
    DB readOnly { implicit session =>
      statusRepository.getByWorldIdOld(worldId, statusId) match {
        case Failure(e) => Left(e)
        case Success(s) => Right(s)
      }
    }

  /**
    * キャラクター別に投稿を取得
    * @param characterId
    * @return
    */
  def getByCharacterId(worldId: Long, characterId: String): Either[Throwable, List[Status]] =
    DB readOnly { implicit session =>
      statusRepository.getByCharacterId(worldId, characterId) match {
        case Failure(e) => Left(e)
        case Success(s) => Right(s)
      }
    }

  /**
    * 指定された個所から最新までの投稿を取得
    * @param id
    * @return
    */
  def getToLast(id: Long): Either[Throwable, List[Status]] =
    DB readOnly { implicit session =>
      val status: Long = statusRepository.getStatus.length
      statusRepository.getToLast(id, status) match {
        case Failure(e) => Left(e)
        case Success(s) => Right(s)
      }
    }
}

trait UsesStatusService {
  val statusService: StatusService
}

trait MixInStatusService {
  val statusService: StatusService = new StatusService with MixInStatusRepository
}
