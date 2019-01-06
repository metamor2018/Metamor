package mocks

import models.repository.CharacterRepository
import models.service.CharacterService

object ErrorCharacterRepositoryImpl extends CharacterRepository {
  def create(id: String, creatorId: String, name: String): Long = throw new Exception

  def delete(id: String): Long = throw new Exception

  def exists(characterId: String): Boolean = false

  def edit(id: String, name: String, profile: String, icon: String): Long = throw new Exception
}

trait MixInErrorCharacterRepository {
  val characterRepository: CharacterRepository = ErrorCharacterRepositoryImpl
}

trait MixInErrorCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInErrorCharacterRepository
}
