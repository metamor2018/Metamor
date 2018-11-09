package models.repository

import scalikejdbc._

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
