package models.repository

trait CreatorRepository {
  def create(displayId: String, name: String): Long
}

trait UsesCreatorRepository extends CreatorRepository {
  val creatorRepository: CreatorRepository
}
