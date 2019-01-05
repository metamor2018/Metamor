package models.service

import models.repository.{ MixInCharacterRepository, UsesCharacterRepository }
import models.entity.Character

trait CharacterService extends UsesCharacterRepository {

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
  def getByCreatorId(creatorId: String): List[Character] = {
    characterRepository.getByCreatorId(creatorId)
  }

}

trait UsesCharacterService {
  val characterService: CharacterService
}

trait MixInCharacterService {
  val characterService: CharacterService = new CharacterService with MixInCharacterRepository
}
