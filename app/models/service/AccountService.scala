package models.service

import models.repository.{ MixInAccountRepository, UsesAccountRepository }

trait AccountService extends UsesAccountRepository {
  def exists(authId: String): Boolean = accountRepository.exists(authId)
  def create(authId: String): Long = accountRepository.create(authId)
}

trait UsesAccountService {
  val accountService: AccountService
}

trait MixInAccountService {
  val accountService: AccountService = new AccountService with MixInAccountRepository
}
