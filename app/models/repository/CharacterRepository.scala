package models.repository

trait CharacterRepository {
  def create(creatorId: String, displayId: String, name: String): Long
}

trait UsesCharacterRepository extends CharacterRepository {
  val characterRepository: CharacterRepository
}
