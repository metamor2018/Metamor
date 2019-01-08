package models.service

import models.entity.Character
import models.repository.{ MixInCharacterRepository, UsesCharacterRepository }
import scalikejdbc.DB
import scala.util.{ Failure, Success }

trait CharacterService extends UsesCharacterRepository {

  /**
   * idからキャラクターを1件取得
   * @param id
   * @return
   */
  def find(id: String): Either[Throwable, Option[Character]] =
    DB readOnly { implicit s =>
      characterRepository.find(id) match {
        case Failure(e) => Left(e)
        case Success(s) => Right(s)
      }
    }

  /**
   *創作者がキャラクターを作成する
   * @param creatorId　創作者ID
   * @param displayId　表示するID
   * @param name 名前
   * @return　作成したキャラクターの主キー
   */
  def create(id: String, creatorId: String, name: String): Long = {
    characterRepository.create(id, creatorId, name)
  }

  /**
   * 創作者がキャラクターを削除する
   * @param id　キャラクターID
   * @return　削除するキャラクターID
   */
  def delete(id: String): Long = {
    characterRepository.delete(id)
  }

  /**
   * キャラクターが存在することの確認
   * @param characterId
   * @return 存在すればtrue
   */
  def exists(id: String): Boolean =
    characterRepository.exists(id)

  /**
   * @param creatorId
   * @return 指定した創作者のキャラクター一覧
   */
  def getByCreatorId(creatorId: String, line: Long): List[Character] = {
    characterRepository.getByCreatorId(creatorId, line)
  }

}

trait UsesCharacterService {
  val characterService: CharacterService
}

trait MixInCharacterService {
  val characterService: CharacterService = new CharacterService with MixInCharacterRepository
}
