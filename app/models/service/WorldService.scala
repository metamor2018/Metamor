package models.service

import java.time.ZonedDateTime

import models.entity.World
import models.repository.{ MixInWorldRepository, UsesWorldRepository }

abstract class WorldService extends UsesWorldRepository {

  /**
   * ワールドを作成する
   * @param name 作成したワールドの名前
   * @param creatorId　ワールドを作成した創造者ID
   * @param detail　ワールド詳細
   * @param startedAt　開始日
   * @return　作成したワールドの主キー
   */
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = {
    worldRepository.create(name, creatorId, detail, startedAt)
  }

  /**
   * ワールド一覧を取得する
   * @return 存在するワールドの一覧
   */
  def getWorlds(): List[World] = {
    worldRepository.getWorlds()
  }

  /**
   * 開催中のワールド一覧を取得する
   * @return 存在する開催中のワールドの一覧
   */
  def getEnable(): List[World] = {
    worldRepository.getEnable()
  }
}

trait UsesWorldService {
  val worldService: WorldService
}

trait MixInWorldService {
  val worldService: WorldService = new WorldService with MixInWorldRepository
}
