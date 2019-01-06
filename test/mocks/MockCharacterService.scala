package mocks

import models.repository.CharacterRepository
import models.service.CharacterService

object MockCharacterRepositoryImpl extends CharacterRepository {
  def create(id: String, creatorId: String, name: String): Long = 1

  def delete(id: String): Long = 1

  def exists(characterId: String): Boolean = true

  def edit(id: String, name: String, profile: String, icon: String): Long = 1
}

trait MixInMockCharacterRepository {
  val characterRepository: CharacterRepository = MockCharacterRepositoryImpl
}

trait MixInMockCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInMockCharacterRepository
}
