package mocks

import models.entity.Account
import models.repository.AccountRepository
import models.service.AccountService
import scalikejdbc.DBSession

import scala.util.Try

object ErrorAccountRepositoryImpl extends AccountRepository {

  def exists(authId: String): Boolean = true

  def create(authId: String): Long = 1

  /**
    * AuthIdからAccountを取得
    *
    * @param authId
    * @return 該当するAccount
    */
  def findByAuthId(authId: String)(implicit s: DBSession): Try[Option[Account]] =
    Try(throw new Exception)
}

trait MixInErrorAccountRepository {
  val accountRepository: AccountRepository = ErrorAccountRepositoryImpl
}

trait MixInErrorAccountService {
  val mockAccountService: AccountService = new AccountService with MixInErrorAccountRepository
}
