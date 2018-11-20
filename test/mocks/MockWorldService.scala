package mocks

import java.time.ZonedDateTime
import models.repository.WorldRepository
import models.service.WorldService

object MockWorldRepositoryImpl extends WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = 1

}

trait MixInMockWorldRepository {
  val worldRepository: WorldRepository = MockWorldRepositoryImpl
}

trait MixInMockWorldService {
  val mockWorldService: WorldService = new WorldService with MixInMockWorldRepository {}
}
