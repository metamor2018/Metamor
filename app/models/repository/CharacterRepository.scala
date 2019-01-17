package models.repository

import models.entity.Character
import scalikejdbc._

import scala.util.control.Exception.catching
import models.entity.Character

import scala.util.Try

trait CharacterRepository {

  /**
    * idからキャラクターを1件取得
    * @param id
    * @param s
    * @return
    */
  def find(id: String)(implicit s: DBSession): Try[Option[Character]]

  /**
    * キャラクターの作成
    * @param id
    * @param creatorId
    * @param name
    * @param s
    * @return
    */
  def create(id: String, creatorId: String, name: String)(implicit s: DBSession): Try[Long]
  def delete(id: String): Long
  def exists(characterId: String): Boolean
  def getByCreatorId(creatorId: String, line: Long): List[Character]
}

trait UsesCharacterRepository {
  val characterRepository: CharacterRepository
}

trait MixInCharacterRepository {
  val characterRepository: CharacterRepository = CharacterRepositoryImpl
}

object CharacterRepositoryImpl extends CharacterRepository {

  /**
    * idからキャラクターを1件取得
    * @param id
    * @param s
    * @return
    */
  def find(id: String)(implicit s: DBSession): Try[Option[Character]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT ch.*,
                   cr.account_id,
                   cr.name AS creator_name,
                   cr.profile AS creator_profile,
                   cr.icon AS creator_icon,
                   cr.official AS creator_official,
                   cr.deleted_at AS creator_deleted_at,
                   cr.updated_at AS creator_updated_at,
                   cr.created_at AS creator_created_at
            FROM characters as ch
            JOIN creators cr on ch.creator_id = cr.id
            WHERE ch.id = $id
         """.map(Character.*).single().apply()

  /**
    * キャラクターの作成
    * @param id
    * @param creatorId
    * @param name
    * @param s
    * @return
    */
  def create(id: String, creatorId: String, name: String)(implicit s: DBSession): Try[Long] =
    catching(classOf[Throwable]) withTry
      sql"""
           insert into characters(id,creator_id,name)
           values (${id},${creatorId}, ${name})
        """.update().apply()

  def delete(id: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
            DELETE FROM characters where id=${id}
      """.update().apply()
    }
  }

  def exists(id: String): Boolean = {
    DB readOnly { implicit session =>
      sql"""
            SELECT id FROM characters WHERE id=${id}
        """.map(_.string("id")).first().apply().isDefined
    }
  }

  def getByCreatorId(creatorId: String, line: Long): List[Character] = {
    DB readOnly { implicit session =>
      sql"""
           SELECT ch.*,
                  cr.account_id,
                  cr.name AS creator_name,
                  cr.profile AS creator_profile,
                  cr.icon AS creator_icon,
                  cr.official AS creator_official,
                  cr.deleted_at AS creator_deleted_at,
                  cr.updated_at AS creator_updated_at,
                  cr.created_at AS creator_created_at
           FROM characters as ch
           JOIN creators cr on ch.creator_id = cr.id
           WHERE ch.creator_id=${creatorId}
           LIMIT ${line * 10 - 10}, 10
        """
        .map(Character.*)
        .list()
        .apply()
    }
  }
}
