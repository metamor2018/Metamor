package models.repository
import java.time.ZonedDateTime

import models.entity.World
import scalikejdbc._

trait WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long

  def exists(worldId: Long): Boolean

  def getWorlds(): List[World]

  def entry(characterId: Long, worldId: Long): Long

  def existsEntry(characterId: Long, worldId: Long): Boolean

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

  def exists(worldId: Long): Boolean = {
    DB readOnly { implicit session =>
      sql"""
             SELECT id
             FROM worlds
             WHERE id = ${worldId}
        """.map(rs => rs.string("id")).first().apply().isDefined
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

  def entry(characterId: Long, worldId: Long): Long = {
    DB autoCommit { implicit session =>
      sql"""
         insert into worlds_entries(character_Id,world_Id)
         values (${characterId},${worldId})
       """.updateAndReturnGeneratedKey().apply()
    }
  }

  def existsEntry(characterId: Long, worldId: Long): Boolean = {
    DB readOnly { implicit session =>
      sql"""
            SELECT id
            FROM worlds_entries
            WHERE character_id=${characterId}
            AND world_id=${worldId}
        """.map(rs => rs.long("id")).first().apply().isDefined
    }
  }
}
