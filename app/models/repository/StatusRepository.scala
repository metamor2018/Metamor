package models.repository

import models.entity.Status
import scalikejdbc._

import scala.util.Try
import scala.util.control.Exception._

trait StatusRepository {

  /**
   * 投稿の作成
   * @param worldId
   * @param characterId
   * @param reply
   * @param inReplyToId
   * @param text
   * @param s
   * @return 作成した投稿のid
   */
  def create(worldId: Long,
             characterId: String,
             reply: Boolean,
             inReplyToId: Option[Long],
             text: String)(implicit s: DBSession): Try[Long]

  /**
   * idから投稿を取得
   * @param statusId
   * @param s
   * @return 投稿
   */
  def find(statusId: Long)(implicit s: DBSession): Try[Option[Status]]

  /**
   * 投稿が存在するか確認
   * @param statusId
   * @param s
   * @return 存在すればtrue
   */
  def exists(statusId: Long)(implicit s: DBSession): Try[Boolean]
}

trait UsesStatusRepository {
  val statusRepository: StatusRepository
}

trait MixInStatusRepository {
  val statusRepository: StatusRepository = StatusRepositoryImpl
}

object StatusRepositoryImpl extends StatusRepository {

  def create(worldId: Long,
             characterId: String,
             reply: Boolean,
             inReplyToId: Option[Long],
             text: String)(implicit s: DBSession): Try[Long] =
    catching(classOf[Throwable]) withTry
      sql"""
            INSERT INTO statuses(world_id, character_id, reply, in_reply_to_id, text)
            VALUES ($worldId, $characterId, $reply, $inReplyToId, $text)
      """.updateAndReturnGeneratedKey().apply()

  def find(statusId: Long)(implicit s: DBSession): Try[Option[Status]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT * FROM statuses
      """.map(Status.*).single().apply()

  /**
   * 投稿が存在するか確認
   *
   * @param statusId
   * @param s
   * @return 存在すればtrue
   */
  def exists(statusId: Long)(implicit s: DBSession): Try[Boolean] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT id FROM statuses WHERE id = $statusId
      """.map(_.long("id")).single().apply().isDefined
}
