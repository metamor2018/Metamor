package models.service

import models.repository.{ MixInCreatorRepository, UsesCreatorRepository }

trait CreatorService extends UsesCreatorRepository {

  /**
   * クリエイターを作成する
   * @param displayId 表示するid
   * @param name 名前
   * @return 作成したクリエイターの主キー
   */
  def create(displayId: String, name: String): Long = {
    creatorRepository.create(displayId, name)
  }

  def existsByDisplayId(displayId: String): Boolean = {
    creatorRepository.existsByDisplayId(displayId)
  }

  /**
   * 創作者が存在することをauthIdから確認
   * @param authId
   * @return 存在すればtrue
   */
  def existsByAuthId(authId: String): Boolean = {
    creatorRepository.existsByAuthId(authId)
  }
}

trait UsesCreatorService {
  val creatorService: CreatorService
}

trait MixInCreatorService {
  val creatorService: CreatorService = new CreatorService with MixInCreatorRepository
}
