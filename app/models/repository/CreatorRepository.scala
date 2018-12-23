package models.repository

import scalikejdbc._

trait CreatorRepository {
  def create(id: String, name: String): Long
  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long
  def existsById(id: String): Boolean
  def existsByAuthId(authId: String): Boolean
}

trait UsesCreatorRepository {
  val creatorRepository: CreatorRepository
}

trait MixInCreatorRepository {
  val creatorRepository: CreatorRepository = CreatorRepositoryImpl
}

object CreatorRepositoryImpl extends CreatorRepository {

  def create(id: String, name: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into creators(id,name)
           values (${id}, ${name})
        """.updateAndReturnGeneratedKey().apply()
    }
  }

  def existsById(id: String): Boolean =
    DB readOnly { implicit session =>
      sql"""
            SELECT id
            FROM creators
            WHERE id = $id
        """.map(rs => rs.string("id")).single().apply().isDefined
    }

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
        update creators
        set display_id=${displayId},name=${name},profile=${profile},icon=${icon}
        where id=${id}
      """.update().apply()
    }
  }

  def existsByAuthId(authId: String): Boolean =
    DB readOnly { implicit session =>
      sql"""
            SELECT a.auth_id
            FROM creators
            JOIN accounts a ON creators.account_id = a.id
         """.map(_.string("auth_id")).first().apply().isDefined
    }
}
