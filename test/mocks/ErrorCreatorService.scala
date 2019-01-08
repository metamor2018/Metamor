package mocks

import models.entity.Creator
import models.repository.CreatorRepository
import models.service.CreatorService
import scalikejdbc.DBSession

import scala.util.Try

object ErrorCreatorRepositoryImpl extends CreatorRepository {
  def create(displayId: String, name: String): Long = throw new Exception

  /**
    * 創作者を1件取得
    *
    * @param id
    */
  def find(id: String)(implicit s: DBSession): Try[Option[Creator]] = Try(throw new Exception)

  def existsById(id: String): Boolean = false

  def edit(id: Long, displayId: String, name: String, profile: String, icon: String): Long =
    throw new Exception

  def existsByAuthId(authId: String): Boolean = false

  def create(id: String, name: String, accountId: Long)(implicit s: DBSession): Try[Long] =
    Try(throw new Exception)

}

trait MixInErrorCreatorRepository {
  val creatorRepository: CreatorRepository = ErrorCreatorRepositoryImpl
}

trait MixInErrorCreatorService {
  val mockCreatorService: CreatorService = new CreatorService with MixInErrorCreatorRepository
  with MixInErrorAccountRepository
}
