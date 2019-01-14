package models.repository

import models.entity.Account
import scalikejdbc._

import scala.util.Try
import scala.util.control.Exception._

trait AccountRepository {
  def exists(authId: String): Boolean
  def create(authId: String): Long

  /**
    * AuthIdからAccountを取得
    * @param authId
    * @return 該当するAccount
    */
  def findByAuthId(authId: String)(implicit s: DBSession): Try[Option[Account]]
}

trait UsesAccountRepository {
  val accountRepository: AccountRepository
}

trait MixInAccountRepository {
  val accountRepository: AccountRepository = AccountRepositoryImpl
}

object AccountRepositoryImpl extends AccountRepository {

  def exists(authId: String): Boolean = {
    DB readOnly { implicit session =>
      sql"""
            SELECT id
            FROM accounts
            WHERE auth_id = ${authId}
        """.map(rs => rs.string("id")).first().apply().isDefined
    }
  }

  def create(authId: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
            insert into accounts (auth_id) values (${authId})
        """.updateAndReturnGeneratedKey().apply()
    }
  }

  /**
    * AuthIdからAccountを取得
    *
    * @param authId
    * @return 該当するAccount
    */
  def findByAuthId(authId: String)(implicit s: DBSession): Try[Option[Account]] =
    catching(classOf[Throwable]) withTry
      sql"""
          SELECT *
          FROM accounts
          WHERE auth_id = ${authId}
      """.map(Account.*).first().apply()
}
