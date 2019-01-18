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

  /**
    * 最新の投稿を20件取得
    * @param s
    */
  def getByWorldId(worldId: Long)(implicit s: DBSession): Try[List[Status]]

  /**
    * statusIdから、古い投稿を20件取得
    * @param worldId
    * @param statusId
    * @param s
    * @return
    */
  def getByWorldIdOld(worldId: Long, statusId: Long)(implicit s: DBSession): Try[List[Status]]

  /**
    * キャラクター別に投稿を取得
    * @param characterId
    * @param s
    * @return
    */
  def getByCharacterId(worldId: Long, characterId: String)(implicit s: DBSession): Try[List[Status]]

  /**
    * 指定された個所から最新までの投稿を取得
    * @param id
    * @param status
    * @param s
    * @return
    */
  def getToLast(id: Long, statusId: Long)(implicit s: DBSession): Try[List[Status]]
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
            SELECT s.*,
                   ch.creator_id,
                   cr.account_id,
                   ch.name AS character_name,
                   ch.profile AS character_profile,
                   ch.icon AS character_icon,
                   ch.deleted_at AS character_deleted_at,
                   ch.updated_at AS character_updated_at,
                   ch.created_at AS character_created_at,
                   cr.name AS creator_name,
                   cr.profile AS creator_profile,
                   cr.icon AS creator_icon,
                   cr.official AS creator_official,
                   cr.deleted_at AS creator_deleted_at,
                   cr.updated_at AS creator_updated_at,
                   cr.created_at AS creator_created_at
            FROM statuses as s
            JOIN characters ch on s.character_id = ch.id
            JOIN creators cr on ch.creator_id = cr.id
            WHERE s.id = ${statusId}
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

  /**
    * 最新の投稿を20件取得
    * @param s
    */
  def getByWorldId(worldId: Long)(implicit s: DBSession): Try[List[Status]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT s.*,
                   ch.creator_id,
                   cr.account_id,
                   ch.name AS character_name,
                   ch.profile AS character_profile,
                   ch.icon AS character_icon,
                   ch.deleted_at AS character_deleted_at,
                   ch.updated_at AS character_updated_at,
                   ch.created_at AS character_created_at,
                   cr.name AS creator_name,
                   cr.profile AS creator_profile,
                   cr.icon AS creator_icon,
                   cr.official AS creator_official,
                   cr.deleted_at AS creator_deleted_at,
                   cr.updated_at AS creator_updated_at,
                   cr.created_at AS creator_created_at
            FROM statuses as s
            JOIN characters ch on s.character_id = ch.id
            JOIN creators cr on ch.creator_id = cr.id
            WHERE world_id = ${worldId}
            ORDER BY id DESC
            LIMIT 0, 20
      """.map(Status.*).list.apply()

  /**
    * statusIdから、古い投稿を20件取得
    * @param worldId
    * @param statusId
    * @param s
    * @return
    */
  def getByWorldIdOld(worldId: Long, statusId: Long)(implicit s: DBSession): Try[List[Status]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT s.*,
                   ch.creator_id,
                   cr.account_id,
                   ch.name AS character_name,
                   ch.profile AS character_profile,
                   ch.icon AS character_icon,
                   ch.deleted_at AS character_deleted_at,
                   ch.updated_at AS character_updated_at,
                   ch.created_at AS character_created_at,
                   cr.name AS creator_name,
                   cr.profile AS creator_profile,
                   cr.icon AS creator_icon,
                   cr.official AS creator_official,
                   cr.deleted_at AS creator_deleted_at,
                   cr.updated_at AS creator_updated_at,
                   cr.created_at AS creator_created_at
            FROM statuses as s
            JOIN characters ch on s.character_id = ch.id
            JOIN creators cr on ch.creator_id = cr.id
            WHERE world_id = ${worldId}
            AND s.id < ${statusId}
            ORDER BY id DESC
            LIMIT 0, 20
      """.map(Status.*).list.apply()

  def getByCharacterId(worldId: Long, characterId: String)(
      implicit s: DBSession): Try[List[Status]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT s.*,
                   ch.creator_id,
                   cr.account_id,
                   ch.name AS character_name,
                   ch.profile AS character_profile,
                   ch.icon AS character_icon,
                   ch.deleted_at AS character_deleted_at,
                   ch.updated_at AS character_updated_at,
                   ch.created_at AS character_created_at,
                   cr.name AS creator_name,
                   cr.profile AS creator_profile,
                   cr.icon AS creator_icon,
                   cr.official AS creator_official,
                   cr.deleted_at AS creator_deleted_at,
                   cr.updated_at AS creator_updated_at,
                   cr.created_at AS creator_created_at
            FROM statuses as s
            JOIN characters ch on s.character_id = ch.id
            JOIN creators cr on ch.creator_id = cr.id
            WHERE character_id = $characterId AND world_id = $worldId
            ORDER BY created_at DESC
      """.map(Status.*).list.apply()

  def getToLast(id: Long, statusId: Long)(implicit s: DBSession): Try[List[Status]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT s.*,
                   ch.creator_id,
                   cr.account_id,
                   ch.name AS character_name,
                   ch.profile AS character_profile,
                   ch.icon AS character_icon,
                   ch.deleted_at AS character_deleted_at,
                   ch.updated_at AS character_updated_at,
                   ch.created_at AS character_created_at,
                   cr.name AS creator_name,
                   cr.profile AS creator_profile,
                   cr.icon AS creator_icon,
                   cr.official AS creator_official,
                   cr.deleted_at AS creator_deleted_at,
                   cr.updated_at AS creator_updated_at,
                   cr.created_at AS creator_created_at
            FROM statuses as s
            JOIN characters ch on s.character_id = ch.id
            JOIN creators cr on ch.creator_id = cr.id
            WHERE world_id = ${id}
            AND s.id > ${statusId}
            ORDER BY id DESC
      """.map(Status.*).list.apply()
}
