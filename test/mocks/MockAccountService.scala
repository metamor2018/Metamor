package mocks

import models.repository.AccountRepository
import models.service.AccountService

object MockAccountRepositoryImpl extends AccountRepository {

  def exists(authId: String): Boolean = true

  def create(authId: String): Long = 1
}

trait MixInMockAccountRepository {
  val accountRepository: AccountRepository = MockAccountRepositoryImpl
}

trait MixInMockAccountService {
  val mockAccountService: AccountService = new AccountService with MixInMockAccountRepository {}
}
