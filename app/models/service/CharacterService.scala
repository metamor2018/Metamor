package models.service

import models.entity.Character
import models.repository.{ MixInCharacterRepository, UsesCharacterRepository }
import scalikejdbc.DB
import scala.util.{ Failure, Success, Try }
import scalikejdbc.TxBoundary.Try._

trait CharacterService extends UsesCharacterRepository {

  /**
    * idからキャラクターを1件取得
    * @param id
    * @return
    */
  def find(id: String): Either[Throwable, Option[Character]] =
    DB readOnly { implicit s =>
      characterRepository.find(id) match {
        case Failure(e) => Left(e)
        case Success(s) => Right(s)
      }
    }

  /**
    * キャラクターの作成
    * @param id
    * @param creatorId
    * @param name
    * @return 作成したキャラクター
    */
  def create(id: String, creatorId: String, name: String): Either[Throwable, Character] =
    DB localTx { implicit s =>
      for {
        _ <- characterRepository.create(id, creatorId, name)
        characterOpt <- characterRepository.find(id)
        character <- Try(characterOpt.get)
      } yield character
    } match {
      case Failure(e) => Left(e)
      case Success(s) => Right(s)
    }

  /**
    * 創作者がキャラクターを削除する
    * @param id　キャラクターID
    * @return　削除するキャラクターID
    */
  def delete(id: String): Long = {
    characterRepository.delete(id)
  }

  /**
    * キャラクターの情報を編集する
    * @param id 創作者が決めたキャラクターの一意なid
    * @param name キャラクターの名前
    * @param profile キャラクターのプロフィール
    * @param icon キャラクターのアイコン
    * @return 編集したキャラクターの主キー
    */
  def edit(id: String, name: String, profile: String, icon: String): Long = {
    characterRepository.edit(id, name, profile, icon)
  }

  /**
    * キャラクターが存在することの確認
    * @param characterId
    * @return 存在すればtrue
    */
  def exists(id: String): Boolean =
    characterRepository.exists(id)

  /**
    * @param creatorId
    * @return 指定した創作者のキャラクター一覧
    */
  def getByCreatorId(creatorId: String, line: Long): List[Character] = {
    characterRepository.getByCreatorId(creatorId, line)
  }

}

trait UsesCharacterService {
  val characterService: CharacterService
}

trait MixInCharacterService {
  val characterService: CharacterService = new CharacterService with MixInCharacterRepository
}
