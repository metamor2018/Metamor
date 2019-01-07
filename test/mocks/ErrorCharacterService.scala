package mocks

import models.entity.Character
import models.repository.CharacterRepository
import models.service.CharacterService
import scalikejdbc.DBSession
import scala.util.Try

object ErrorCharacterRepositoryImpl extends CharacterRepository {

  /**
    * idからキャラクターを1件取得
    *
    * @param id
    * @param s
    * @return
    */
  def find(id: String)(implicit s: DBSession): Try[Option[Character]] =
    Try(throw new Exception)

  def create(id: String, creatorId: String, name: String): Long = throw new Exception

  def delete(id: String): Long = throw new Exception

  def exists(characterId: String): Boolean = false

  def getByCreatorId(creatorId: String): List[Character] = throw new Exception
}

trait MixInErrorCharacterRepository {
  val characterRepository: CharacterRepository = ErrorCharacterRepositoryImpl
}

trait MixInErrorCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInErrorCharacterRepository
}
