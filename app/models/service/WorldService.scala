package models.service

import java.time.ZonedDateTime
import models.repository.{ MixInWorldRepository, UsesWorldRepository }

abstract class WorldService extends UsesWorldRepository {

  /**
   * ワールドを作成する
   * @param name 作成したワールドの名前
   * @param worldId　ワールドを作成した創造者ID
   * @param detail　ワールド詳細
   * @param startedAt　開始日
   * @return　作成したワールドの主キー
   */
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = {
    worldRepository.create(name, creatorId, detail, startedAt)
  }
}

trait UsesWorldService {
  val worldService: WorldService
}

trait MixInWorldService {
  val worldService: WorldService = new WorldService with MixInWorldRepository
}
