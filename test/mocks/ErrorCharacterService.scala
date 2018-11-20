package mocks

import models.repository.CharacterRepository
import models.service.CharacterService

object ErrorCharacterRepositoryImpl extends CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long = throw new Exception
}

trait MixInErrorCharacterRepository {
  val characterRepository: CharacterRepository = ErrorCharacterRepositoryImpl
}

trait MixInErrorCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInErrorCharacterRepository {}
}
