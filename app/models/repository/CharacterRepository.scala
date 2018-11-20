package models.repository

import scalikejdbc._

trait CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long
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

}
