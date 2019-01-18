package mocks

import java.time.ZonedDateTime

import models.entity.{ Character, Creator }
import models.repository.CharacterRepository
import models.service.CharacterService
import scalikejdbc.DBSession

import scala.util.Try

object MockCharacterRepositoryImpl extends CharacterRepository {

  /**
    * idからキャラクターを1件取得
    *
    * @param id
    * @param s
    * @return
    */
  def find(id: String)(implicit s: DBSession): Try[Option[Character]] =
    Try(
      Some(
        Character(
          "hoge",
          Creator(
            "hoge",
            1,
            "なまえ",
            None,
            None,
            false,
            None,
            ZonedDateTime.now(),
            ZonedDateTime.now()
          ),
          "なまえ",
          None,
          None,
          None,
          ZonedDateTime.now(),
          ZonedDateTime.now()
        )))

  def create(id: String,
             creatorId: String,
             name: String,
             profile: Option[String],
             icon: Option[String])(implicit s: DBSession): Try[Long] =
    Try(1)

  def delete(id: String): Long = 1

  def exists(characterId: String): Boolean = true

  def getByCreatorId(creatorId: String, line: Long): List[Character] = {
    val character = Character(
      "huge",
      Creator(
        "hoge",
        1,
        "なまえ",
        None,
        None,
        false,
        None,
        ZonedDateTime.now(),
        ZonedDateTime.now()
      ),
      "hugeName",
      None,
      None,
      None,
      ZonedDateTime.now(),
      ZonedDateTime.now()
    )
    List(character, character.copy(name = "geho"))
  }

}

trait MixInMockCharacterRepository {
  val characterRepository: CharacterRepository = MockCharacterRepositoryImpl
}

trait MixInMockCharacterService {
  val mockCharacterService: CharacterService = new CharacterService
  with MixInMockCharacterRepository
}
