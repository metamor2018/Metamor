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
}

trait UsesCreatorService {
  val creatorService: CreatorService
}

trait MixInCreatorService {
  val creatorService: CreatorService = new CreatorService with MixInCreatorRepository
}
