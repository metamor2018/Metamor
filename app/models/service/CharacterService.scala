package models.service

import models.repository.{ MixInCharacterRepository, UsesCharacterRepository }

trait CharacterService extends UsesCharacterRepository {

  /**
   *創作者がキャラクターを作成する
   * @param creatorId　創作者ID
   * @param displayId　表示するID
   * @param name 名前
   * @return　作成したキャラクターの主キー
   */
  def create(creatorId: String, displayId: String, name: String): Long = {
    characterRepository.create(creatorId, displayId, name)
  }

  /**
   * 創作者がキャラクターを削除する
   * @param id　キャラクターID
   * @return　削除するキャラクターID
   */
  def delete(id: Long): Long = {
    characterRepository.delete(id)
  }

  /**
   * キャラクターが存在することの確認
   * @param characterId
   * @return 存在すればtrue
   */
  def exists(characterId: String): Boolean =
    characterRepository.exists(characterId)

}

trait UsesCharacterService {
  val characterService: CharacterService
}

trait MixInCharacterService {
  val characterService: CharacterService = new CharacterService with MixInCharacterRepository
}
