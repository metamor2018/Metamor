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
  def create(id: String, creatorId: String, name: String): Long
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
            SELECT * FROM characters WHERE id = $id
         """.map(Character.*).single().apply()

  def create(id: String, creatorId: String, name: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into characters(id,creator_id,name)
           values (${id},${creatorId}, ${name})
        """.update().apply()
    }
  }

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
            SELECT * FROM characters WHERE creator_id=${creatorId} LIMIT ${line * 10 - 10},${line * 10}
        """
        .map(Character.*)
        .list()
        .apply()
    }
  }
}
