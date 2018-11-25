package models.repository

import scalikejdbc._

import scala.util.Try

trait AccountRepository {
  def exists(authId: String): Boolean
  def create(authId: String): Long
}

trait UsesAccountRepository extends AccountRepository {
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
        """.map(rs => rs.string("id")).single().apply().isDefined
    }
  }

  def create(authId: String): Long = {

    DB autoCommit { implicit session =>
      sql"""
            insert into accounts (auth_id) values (${authId})
        """.updateAndReturnGeneratedKey().apply()
    }
  }
}
