package models.repository

import scalikejdbc._

trait CharacterRepository {
  def create(id: String, creatorId: String, name: String): Long
  def delete(id: Long): Long
  def exists(id: String): Boolean
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

  def delete(id: Long): Long = {
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
}
