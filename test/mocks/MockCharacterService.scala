package mocks

import models.repository.CharacterRepository
import models.service.CharacterService

object MockCharacterRepositoryImpl extends CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long = 1
}

trait MixInMockCharacterRepository {
  val characterRepository: CharacterRepository = MockCharacterRepositoryImpl
}

trait MixInMockCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInMockCharacterRepository {}
}
