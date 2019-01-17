package models.repository

import models.entity.World
import scalikejdbc._

import scala.util.Try
import scala.util.control.Exception.catching

trait WorldRepository {
  def create(name: String, creatorId: String, detail: String)(implicit s: DBSession): Try[Long]

  def exists(worldId: Long): Boolean

  def getWorlds(): List[World]

  def getEnable(): List[World]

  def entry(characterId: String, worldId: Long)(implicit s: DBSession): Try[Long]

  def existsEntry(characterId: String, worldId: Long): Boolean

  def getByCreatorId(creatorId: String)(implicit s: DBSession): Try[List[World]]

  def find(id: Int)(implicit s: DBSession): Try[Option[World]]
}

trait UsesWorldRepository {
  val worldRepository: WorldRepository
}

trait MixInWorldRepository {
  val worldRepository: WorldRepository = WorldRepositoryImpl
}

object WorldRepositoryImpl extends WorldRepository {

  def create(name: String, creatorId: String, detail: String)(implicit s: DBSession): Try[Long] =
    catching(classOf[Throwable]) withTry
      sql"""
           insert into worlds(name,creator_Id,detail)
           values (${name},${creatorId},${detail})
        """.updateAndReturnGeneratedKey().apply()

  def exists(worldId: Long): Boolean = {
    DB readOnly { implicit session =>
      sql"""
             SELECT id
             FROM worlds
             WHERE id = ${worldId}
        """.map(rs => rs.string("id")).single().apply().isDefined
    }
  }

  def getWorlds(): List[World] = {
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM worlds
      """
        .map(World.*)
        .list()
        .apply()
    }
  }

  def getEnable(): List[World] = {
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM worlds WHERE ended_at IS NULL
      """
        .map(World.*)
        .list()
        .apply()
    }
  }

  def getByCreatorId(creatorId: String)(implicit s: DBSession): Try[List[World]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT *
            FROM worlds
            where creator_id = $creatorId
      """
        .map(World.*)
        .list
        .apply()

  def entry(characterId: String, worldId: Long)(implicit s: DBSession): Try[Long] = {
    catching(classOf[Throwable]) withTry
      sql"""
         insert into worlds_entries(character_Id,world_Id)
         values (${characterId},${worldId})
       """.updateAndReturnGeneratedKey().apply()
  }

  def existsEntry(characterId: String, worldId: Long): Boolean = {
    DB readOnly { implicit session =>
      sql"""
            SELECT id
            FROM worlds_entries
            WHERE character_id=${characterId}
            AND world_id=${worldId}
        """.map(rs => rs.string("id")).single().apply().isDefined
    }
  }

  def find(id: Int)(implicit s: DBSession): Try[Option[World]] =
    catching(classOf[Throwable]) withTry
      sql"""
            SELECT *
            FROM worlds
            WHERE id = $id
      """.map(World.*).single().apply()

}
