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

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long = {
    creatorRepository.edit(id, displayId, name, profile, icon)
  }

  def existsById(id: Long): Boolean = {
    creatorRepository.existsById(id)
  }
}

trait UsesCreatorService {
  val creatorService: CreatorService
}

trait MixInCreatorService {
  val creatorService: CreatorService = new CreatorService with MixInCreatorRepository
}
