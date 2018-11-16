package models.service

import java.time.ZonedDateTime

import models.repository.{ MixInWorldRepository, UsesWorldRepository }

trait WorldService extends UsesWorldRepository {

  /**
   * ワールドを
   * @param name 作成したワールドの名前
   * @param creatorId　ワールドを作成した創造者ID
   * @param detail　ワールド詳細
   * @param startedAt　開始日
   * @return　作成したワールドの主キー
   */
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = {
    worldRepository.create(name, creatorId, detail, startedAt)
  }

}

object WorldService extends WorldService with MixInWorldRepository
