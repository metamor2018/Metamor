package repository

import scalikejdbc._

trait UserRepository {

  def exists(userId: String): Boolean
}

trait UsesUserRepository {
  val userRepository: UserRepository
}

trait MixInUserRepository {
  val userRepository: UserRepository = UserRepositoryImpl
}

object UserRepositoryImpl extends UserRepository {

  def exists(userId: String): Boolean = {
    DB readOnly { implicit session =>
      sql"""
            select id
            from users
            where user_id = ${userId}
        """.map(_.int("id")).first().apply() match {
        case Some(_) => true
        case None    => false
      }
    }
  }
}
