package mocks

import models.repository.CreatorRepository
import models.service.CreatorService
import scalikejdbc.DBSession

import scala.util.Try

object MockCreatorRepositoryImpl extends CreatorRepository {
  def create(displayId: String, name: String): Long = 1

  def existsById(id: String): Boolean = true

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long = 1

  def existsByAuthId(authId: String): Boolean = true

  def create(id: String, name: String, accountId: Long)(implicit s: DBSession): Try[Long] =
    Try(1)
}

trait MixInMockCreatorRepository {
  val creatorRepository: CreatorRepository = MockCreatorRepositoryImpl
}

trait MixInMockCreatorService {
  val mockCreatorService: CreatorService = new CreatorService with MixInMockCreatorRepository
  with MixInMockAccountRepository
}
