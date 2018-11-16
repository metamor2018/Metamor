package models.repository
import java.time.ZonedDateTime

trait WorldRepository {
  def create(name: String, creatorId: String, detail: String, startedAt: ZonedDateTime): Long
}

trait UsesWorldRepository extends WorldRepository {
  val worldRepository: WorldRepository
}
