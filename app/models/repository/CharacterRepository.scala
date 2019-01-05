package models.repository

import models.entity.Character
import scalikejdbc._

trait CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long
  def delete(id: String): Long
  def exists(characterId: String): Boolean
  def getByCreatorId(creatorId: String): List[Character]
}

trait UsesCharacterRepository extends CharacterRepository {
  val characterRepository: CharacterRepository
}

trait MixInCharacterRepository {
  val characterRepository: CharacterRepository = CharacterRepositoryImpl
}

object CharacterRepositoryImpl extends CharacterRepository {

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

  def getByCreatorId(creatorId: String): List[Character] = {
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM characters WHERE creator_id=${creatorId}
        """
        .map { rs =>
          Character(
            rs.string("id"),
            rs.string("creator_id"),
            rs.string("name"),
            rs.stringOpt("profile"),
            rs.stringOpt("icon"),
            rs.zonedDateTimeOpt("deleted_at"),
            rs.zonedDateTime("created_at"),
            rs.zonedDateTime("updated_at")
          )
        }
        .list()
        .apply()
    }
  }
}
