package mocks

import java.time.ZonedDateTime

import models.entity.Account
import models.repository.AccountRepository
import models.service.AccountService
import scalikejdbc.DBSession

import scala.util.Try

object MockAccountRepositoryImpl extends AccountRepository {

  def exists(authId: String): Boolean = true

  def create(authId: String)(implicit s: DBSession): Try[Long] = Try(1)

  /**
    * AuthIdからAccountを取得
    *
    * @param authId
    * @return 該当するAccount
    */
  def findByAuthId(authId: String)(implicit s: DBSession): Try[Option[Account]] =
    Try(
      Some(
        Account(
          1,
          "hoge",
          ZonedDateTime.now(),
          ZonedDateTime.now()
        )
      )
    )

}

trait MixInMockAccountRepository {
  val accountRepository: AccountRepository = MockAccountRepositoryImpl
}

trait MixInMockAccountService {
  val mockAccountService: AccountService = new AccountService with MixInMockAccountRepository
  with MixInMockCreatorRepository
}
