package models.repository

import scalikejdbc._

trait CharacterRepository {
  def create(id: String, creatorId: String, name: String): Long
  def delete(id: String): Long
  def edit(id: String, name: String, profile: String, icon: String): Long
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

  def delete(id: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
            DELETE FROM characters where id=${id}
      """.update().apply()
    }
  }

  def edit(id: String, name: String, profile: String, icon: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
        update characters
        set name=${name},profile=${profile},icon=${icon}
        where id=${id}
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
