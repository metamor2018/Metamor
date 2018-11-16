package models.repository

import scalikejdbc._

trait CreatorRepository {
  def create(displayId: String, name: String): Long
}

trait UsesCreatorRepository {
  val creatorRepository: CreatorRepository
}

trait MixInCreatorRepository {
  val creatorRepository: CreatorRepository = CreatorRepositoryImpl
}

object CreatorRepositoryImpl extends CreatorRepository {

  def create(displayId: String, name: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into creators(display_id,name)
           values (${displayId}, ${name})
        """.updateAndReturnGeneratedKey().apply()
    }
  }

}
