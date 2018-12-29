package mocks

import models.repository.CreatorRepository
import models.service.CreatorService

object ErrorCreatorRepositoryImpl extends CreatorRepository {
  def create(displayId: String, name: String): Long = throw new Exception

  def existsById(id: String): Boolean = false

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long =
    throw new Exception

  def existsByAuthId(authId: String): Boolean = false
}

trait MixInErrorCreatorRepository {
  val creatorRepository: CreatorRepository = ErrorCreatorRepositoryImpl
}

trait MixInErrorCreatorService {
  val mockCreatorService: CreatorService = new CreatorService with MixInErrorCreatorRepository
}
