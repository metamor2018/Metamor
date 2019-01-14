package mocks

import models.entity.World
import models.repository.WorldRepository
import models.service.WorldService
import scalikejdbc.DBSession

import scala.util.Try

object ErrorWorldRepositoryImpl extends WorldRepository {
  def create(name: String, creatorId: String, detail: String)(implicit s: DBSession): Try[Long] =
    Try(throw new Exception)
  def getWorlds(): List[World] =
    throw new Exception
  def getEnable(): List[World] =
    throw new Exception
  def getByCreatorId(creatorId: String)(implicit s: DBSession): Try[List[World]] =
    throw new Exception
  def entry(characterId: String, worldId: Long): Long =
    throw new Exception
  def existsEntry(characterId: String, worldId: Long): Boolean =
    throw new Exception
  def exists(worldId: Long): Boolean =
    throw new Exception
  def find(id: Int)(implicit s: DBSession): Try[Option[World]] =
    Try(throw new Exception)
}

trait MixInErrorWorldRepository {
  val worldRepository: WorldRepository = ErrorWorldRepositoryImpl
}

trait MixInErrorWorldService {
  val mockWorldService: WorldService = new WorldService with MixInErrorWorldRepository
}
