package models.repository

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
             inReplyToId: Option[String],
             text: String)(implicit s: DBSession): Try[Long]

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
             inReplyToId: Option[String],
             text: String)(implicit s: DBSession): Try[Long] =
    catching(classOf[Throwable]) withTry
      sql"""
            INSERT INTO statuses(world_id, character_id, reply, text)
            VALUES ($worldId, $characterId, $reply, $text)
      """.updateAndReturnGeneratedKey().apply()
}
