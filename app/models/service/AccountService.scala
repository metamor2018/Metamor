package models.service

import models.repository.{ MixInAccountRepository, UsesAccountRepository }

trait AccountService extends UsesAccountRepository {

  /**
   * authIdがDBに存在することの確認
   * @param authId
   * @return 存在すればtrue
   */
  def exists(authId: String): Boolean = accountRepository.exists(authId)

  /**
   * accountを作成
   * @param authId
   * @return accounts.id
   */
  def create(authId: String): Long = accountRepository.create(authId)
}

trait UsesAccountService {
  val accountService: AccountService
}

trait MixInAccountService {
  val accountService: AccountService = new AccountService with MixInAccountRepository
}
