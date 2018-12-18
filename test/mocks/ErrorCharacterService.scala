package mocks

import models.repository.CharacterRepository
import models.service.CharacterService

object ErrorCharacterRepositoryImpl extends CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long = throw new Exception

  def delete(id: Long): Long = 0

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long =
    throw new Exception

  def exists(characterId: Long): Boolean = false

  def existsByDisplayId(displayId: String): Boolean = true
}

trait MixInErrorCharacterRepository {
  val characterRepository: CharacterRepository = ErrorCharacterRepositoryImpl
}

trait MixInErrorCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInErrorCharacterRepository {}
}
