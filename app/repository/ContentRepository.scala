package repository

import models.Content
import scalikejdbc._

trait ContentRepository {
  def create(userId: String, content: String): Long

  def find(id: Int): Option[Content]
}

trait UsesContentRepository {
  val contentRepository: ContentRepository
}

trait MixInContentRepository {
  val contentRepository: ContentRepository = ContentRepositoryImpl
}

object ContentRepositoryImpl extends ContentRepository {

  def create(userId: String, content: String): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into contents(user_id,content)
           values (${userId}, ${content})
        """.updateAndReturnGeneratedKey().apply()
    }
  }

  def find(id: Int): Option[Content] = {
    DB readOnly { implicit session =>
      sql"""
            select *
            from contents
            where id = ${id}
        """.map(Content.*).single().apply()
    }
  }
}
