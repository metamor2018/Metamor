package models.repository

import scalikejdbc._

trait CreatorRepository {
  def create(displayId: String, name: String): Long
  def existsByDisplayId(displayId: String): Boolean
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

  def existsByDisplayId(displayId: String): Boolean =
    DB readOnly { implicit session =>
      sql"""
            SELECT id
            FROM creators
            WHERE display_id = $displayId
        """.map(rs => rs.string("id")).single().apply().isDefined
    }
}
