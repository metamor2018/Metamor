package service

import models.Content
import repository.{ MixInContentRepository, UsesContentRepository }

trait ContentService extends UsesContentRepository {

  /**
    * 投稿の追加
    * @param userId
    * @param content
    * @return 作成した投稿のid
    */
  def create(userId: String, content: String): Long = {
    contentRepository.create(userId, content)
  }

  /**
    * idから投稿の取得
    * @param id
    * @return 投稿
    */
  def find(id: Int): Option[Content] = {
    contentRepository.find(id)
  }

}

object ContentService extends ContentService with MixInContentRepository
