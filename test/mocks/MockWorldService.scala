package mocks

import java.time.ZonedDateTime

import models.entity.World
import models.repository.WorldRepository
import models.service.WorldService

object MockWorldRepositoryImpl extends WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = 1
  def getWorlds(): List[World] = {
    val world = World(
      "worldId",
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
      "worldId",
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
  def getByCreatorId(creatorId: Long): List[World] = {
    val world = World(
      "worldId",
      "testName",
      "detailtest",
      None,
      None,
      None,
      ZonedDateTime.now(),
      ZonedDateTime.now()
    )
    List(world, world.copy(name = "testName2"), world)
  }
  def entry(characterId: String, worldId: Long): Long = 5
  def existsEntry(characterId: String, worldId: Long): Boolean = true

  def exists(worldId: Long): Boolean = true
}

trait MixInMockWorldRepository {
  val worldRepository: WorldRepository = MockWorldRepositoryImpl
}

trait MixInMockWorldService {
  val mockWorldService: WorldService = new WorldService with MixInMockWorldRepository
}
