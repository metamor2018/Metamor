package models.service

import models.repository.{
  MixInAccountRepository,
  MixInCreatorRepository,
  UsesAccountRepository,
  UsesCreatorRepository
}
import scalikejdbc.DB

import scala.util.{ Failure, Success }

trait AccountService extends UsesAccountRepository with UsesCreatorRepository {

  /**
    * authIdがDBに存在することの確認
    *
    * @param authId
    * @return 存在すればtrue
    */
  def exists(authId: String): Boolean = accountRepository.exists(authId)

  /**
    * accountを作成
    *
    * @param authId
    * @return accounts.id
    */
  def create(authId: String): Either[Throwable, Unit] =
    accountRepository.exists(authId) match {
      case true => Right(())
      case false =>
        DB localTx { implicit s =>
          accountRepository.create(authId) match {
            case Failure(e) => Left(e)
            case Success(_) => Right(())
          }
        }
    }
}

trait UsesAccountService {
  val accountService: AccountService
}

trait MixInAccountService {
  val accountService: AccountService = new AccountService with MixInAccountRepository
  with MixInCreatorRepository
}
