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

}

object CharacterService extends CharacterService with MixInCharacterRepository
