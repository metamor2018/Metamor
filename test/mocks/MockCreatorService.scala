package mocks

import models.repository.CreatorRepository
import models.service.CreatorService

object MockCreatorRepositoryImpl extends CreatorRepository {
  def create(displayId: String, name: String): Long = 1

  def existsByDisplayId(displayId: String): Boolean = true

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long = 1

  def existsById(id: Long): Boolean = true
}

trait MixInMockCreatorRepository {
  val creatorRepository: CreatorRepository = MockCreatorRepositoryImpl
}

trait MixInMockCreatorService {
  val mockCreatorService: CreatorService = new CreatorService with MixInMockCreatorRepository
}
