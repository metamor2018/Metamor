package mocks

import models.repository.CreatorRepository
import models.service.CreatorService

object MockCreatorRepositoryImpl extends CreatorRepository {
  def create(displayId: String, name: String): Long = 1

}

trait MixInMockCreatorRepository {
  val creatorRepository: CreatorRepository = MockCreatorRepositoryImpl
}

trait MixInMockCreatorService {
  val mockCreatorService: CreatorService = new CreatorService with MixInMockCreatorRepository
}
