package mocks

import java.time.ZonedDateTime

import models.entity.World
import models.repository.WorldRepository
import models.service.WorldService

object MockWorldRepositoryImpl extends WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = 1
  def getWorlds(): List[World] = {
    val world = World(
      1,
      "testName",
      1,
      "detailtest",
      None,
      None,
      None,
      ZonedDateTime.now(),
      ZonedDateTime.now()
    )
    List(world, world.copy(name = "testName2"), world)
  }
}

trait MixInMockWorldRepository {
  val worldRepository: WorldRepository = MockWorldRepositoryImpl
}

trait MixInMockWorldService {
  val mockWorldService: WorldService = new WorldService with MixInMockWorldRepository {}
}
