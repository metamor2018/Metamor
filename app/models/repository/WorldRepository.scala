package models.repository
import java.time.ZonedDateTime

import models.entity.World
import scalikejdbc._

trait WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long

  def getWorlds(): List[World]

  def getByCreatorId(creatorId: Long): List[World]
}

trait UsesWorldRepository extends WorldRepository {
  val worldRepository: WorldRepository
}

trait MixInWorldRepository {
  val worldRepository: WorldRepository = WorldRepositoryImpl
}

object WorldRepositoryImpl extends WorldRepository {

  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into worlds(name,creator_Id,detail,started_at)
           values (${name},${creatorId},${detail},${startedAt})
        """.updateAndReturnGeneratedKey().apply()
    }
  }

  def getWorlds(): List[World] = {
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM worlds
      """
        .map { rs =>
          World(
            rs.long("id"),
            rs.string("name"),
            rs.long("creator_id"),
            rs.string("detail"),
            rs.zonedDateTimeOpt("started_at"),
            rs.zonedDateTimeOpt("ended_at"),
            rs.longOpt("emblem_id"),
            rs.zonedDateTime("created_at"),
            rs.zonedDateTime("updated_at")
          )
        }
        .list()
        .apply()
    }
  }

  def getByCreatorId(creatorId: Long): List[World] = {
    DB readOnly { implicit session =>
      sql"""
            SELECT *
            FROM worlds
            where creator_id = $creatorId
      """
        .map(rs =>
          World(
            rs.long("id"),
            rs.string("name"),
            rs.long("creator_id"),
            rs.string("detail"),
            rs.zonedDateTimeOpt("started_at"),
            rs.zonedDateTimeOpt("ended_at"),
            rs.longOpt("emblem_id"),
            rs.zonedDateTime("created_at"),
            rs.zonedDateTime("updated_at")
        ))
        .list
        .apply()
    }
  }
}
