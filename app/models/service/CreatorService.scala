package models.service

import models.repository.{
  MixInAccountRepository,
  MixInCreatorRepository,
  UsesAccountRepository,
  UsesCreatorRepository
}
import scalikejdbc._
import scala.util.{ Failure, Success }

trait CreatorService extends UsesCreatorRepository with UsesAccountRepository {

  /**
   * クリエイターを作成する
   * @param id 表示するid
   * @param name 名前
   * @return 作成したクリエイターの主キー
   */
  def create(id: String, name: String, authId: String): Either[Throwable, Long] = {
    DB localTx { implicit session =>
      (for {
        accountOpt <- accountRepository.findByAuthId(authId)
        creatorId <- creatorRepository.create(id, name, accountOpt.get.id)
      } yield creatorId) match {
        case Failure(e) =>
          session.connection.rollback()
          Left(e)
        case Success(s) =>
          session.connection.commit()
          Right(s)
      }
    }
  }

  /**
   * 創作者が存在するか確認する
   * @param id 確認する創作者のid
   * @return 存在すればtrueしなければfalse
   */
  def existsById(id: String): Boolean = {
    creatorRepository.existsById(id)
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
  val creatorService: CreatorService =
    new CreatorService with MixInCreatorRepository with MixInAccountRepository
}
