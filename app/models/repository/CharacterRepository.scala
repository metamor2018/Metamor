package models.repository

import scalikejdbc._

trait CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long
  def delete(id: Long): Long
  def exists(characterId: Long): Boolean
}

trait UsesCharacterRepository extends CharacterRepository {
  val characterRepository: CharacterRepository
}

trait MixInCharacterRepository {
  val characterRepository: CharacterRepository = CharacterRepositoryImpl
}

object CharacterRepositoryImpl extends CharacterRepository {

  def create(creatorId: String, displayId: String, name: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into characters(creator_id,display_id,name)
           values (${creatorId},${displayId}, ${name})
        """.updateAndReturnGeneratedKey().apply()
    }
  }

  def delete(id: Long): Long = {
    DB autoCommit { implicit session =>
      sql"""
            DELETE FROM characters where id=${id}
      """.update().apply()
    }
  }

  def exists(characterId: Long): Boolean = {
    DB readOnly { implicit session =>
      sql"""
            SELECT id FROM characters WHERE id=${characterId}
        """.map(_.long("id")).first().apply().isDefined
    }
  }
}
