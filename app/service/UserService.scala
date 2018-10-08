package service

import repository.{ MixInUserRepository, UsesUserRepository }

trait UserService extends UsesUserRepository {

  /**
    * ユーザが存在することの確認
    * @param userId
    * @return 存在すればtrue
    */
  def exists(userId: String): Boolean = {
    userRepository.exists(userId)
  }

}

object UserService extends UserService with MixInUserRepository
