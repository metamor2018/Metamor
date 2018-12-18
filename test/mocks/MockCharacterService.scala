package mocks

import models.repository.CharacterRepository
import models.service.CharacterService

object MockCharacterRepositoryImpl extends CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long = 1

  def delete(id: Long): Long = 1

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long = 1

  def exists(characterId: Long): Boolean = true

  def existsByDisplayId(displayId: String): Boolean = true
}

trait MixInMockCharacterRepository {
  val characterRepository: CharacterRepository = MockCharacterRepositoryImpl
}

trait MixInMockCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInMockCharacterRepository {}
}
