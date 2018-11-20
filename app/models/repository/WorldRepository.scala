package models.repository
import java.time.ZonedDateTime

import scalikejdbc._

trait WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long
}

trait UsesWorldRepository extends WorldRepository {
  val worldRepository: WorldRepository
}

trait MixInWorldRepository {
  val worldRepository: WorldRepository = WorldRepositoryImpl
}

object WorldRepositoryImpl extends WorldRepository {

  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long = {
    DB autoCommit { implicit session =>
      sql"""
           insert into worlds(name,creator_Id,detail,started_at)
           values (${name},${creatorId},${detail},${startedAt})
        """.updateAndReturnGeneratedKey().apply()
    }
  }
}
