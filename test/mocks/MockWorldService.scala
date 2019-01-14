package mocks

import java.time.ZonedDateTime

import models.entity.World
import models.repository.WorldRepository
import models.service.WorldService
import scalikejdbc.DBSession

import scala.util.Try

object MockWorldRepositoryImpl extends WorldRepository {
  def create(name: String, creatorId: String, detail: String): Long = 1

  def getWorlds(): List[World] = {
    val world = World(
      1,
      "testName",
      "detailTest",
      None,
      None,
      None,
      ZonedDateTime.now(),
      ZonedDateTime.now()
    )
    List(world, world.copy(name = "testName2"), world)
  }

  def getEnable(): List[World] = {
    val world = World(
      1,
      "testName",
      "detailTest",
      None,
      Some(ZonedDateTime.now()),
      None,
      ZonedDateTime.now(),
      ZonedDateTime.now()
    )
    List(world, world.copy(name = "testName2"), world)
  }
  def getByCreatorId(creatorId: String)(implicit s: DBSession): Try[List[World]] = {
    val world = World(
      1,
      "testName",
      "detailtest",
      None,
      None,
      None,
      ZonedDateTime.now(),
      ZonedDateTime.now()
    )
    Try(List(world, world.copy(id = 2), world.copy(id = 3)))
  }
  def entry(characterId: String, worldId: Long): Long = 5
  def existsEntry(characterId: String, worldId: Long): Boolean = true

  def exists(worldId: Long): Boolean = true

  def find(id: Int)(implicit s: DBSession): Try[Option[World]] =
    Try(
      Some(
        World(
          1,
          "testName",
          "detailtest",
          None,
          None,
          None,
          ZonedDateTime.now(),
          ZonedDateTime.now()
        )))
}

trait MixInMockWorldRepository {
  val worldRepository: WorldRepository = MockWorldRepositoryImpl
}

trait MixInMockWorldService {
  val mockWorldService: WorldService = new WorldService with MixInMockWorldRepository
}
