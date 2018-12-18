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
   * 創作者の情報を編集する
   * @param id 一意に振り分けられた創作者のid
   * @param displayId 創作者が設定する表示id
   * @param name 創作者の名前
   * @param profile 創作者のプロフィール
   * @param icon 創作者のアイコン
   * @return 編集した創作者の主キー
   */
  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long = {
    creatorRepository.edit(id, displayId, name, profile, icon)
  }

  /**
   * 創作者が存在するか確認する
   * @param id 確認する創作者のid
   * @return 存在すればtrueしなければfalse
   */
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
