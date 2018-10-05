package models

import scalikejdbc._
import java.time.{ ZonedDateTime }

case class Favorite(id: Int,
                    userId: String,
                    contentId: Int,
                    createdAt: ZonedDateTime,
                    updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = Favorite.autoSession): Favorite =
    Favorite.save(this)(session)

  def destroy()(implicit session: DBSession = Favorite.autoSession): Int =
    Favorite.destroy(this)(session)

}

object Favorite extends SQLSyntaxSupport[Favorite] {

  override val tableName = "favorites"

  override val columns = Seq("id", "user_id", "content_id", "created_at", "updated_at")

  def apply(f: SyntaxProvider[Favorite])(rs: WrappedResultSet): Favorite = apply(f.resultName)(rs)
  def apply(f: ResultName[Favorite])(rs: WrappedResultSet): Favorite = new Favorite(
    id = rs.get(f.id),
    userId = rs.get(f.userId),
    contentId = rs.get(f.contentId),
    createdAt = rs.get(f.createdAt),
    updatedAt = rs.get(f.updatedAt)
  )

  val f = Favorite.syntax("f")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Favorite] = {
    withSQL {
      select.from(Favorite as f).where.eq(f.id, id)
    }.map(Favorite(f.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Favorite] = {
    withSQL(select.from(Favorite as f)).map(Favorite(f.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Favorite as f)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Favorite] = {
    withSQL {
      select.from(Favorite as f).where.append(where)
    }.map(Favorite(f.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Favorite] = {
    withSQL {
      select.from(Favorite as f).where.append(where)
    }.map(Favorite(f.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Favorite as f).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(userId: String, contentId: Int, createdAt: ZonedDateTime, updatedAt: ZonedDateTime)(
      implicit session: DBSession = autoSession): Favorite = {
    val generatedKey = withSQL {
      insert
        .into(Favorite)
        .namedValues(
          column.userId -> userId,
          column.contentId -> contentId,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    Favorite(
      id = generatedKey.toInt,
      userId = userId,
      contentId = contentId,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(entities: collection.Seq[Favorite])(
      implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(
      entity =>
        Seq(
          'userId -> entity.userId,
          'contentId -> entity.contentId,
          'createdAt -> entity.createdAt,
          'updatedAt -> entity.updatedAt
      ))
    SQL("""insert into favorites(
      user_id,
      content_id,
      created_at,
      updated_at
    ) values (
      {userId},
      {contentId},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Favorite)(implicit session: DBSession = autoSession): Favorite = {
    withSQL {
      update(Favorite)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.contentId -> entity.contentId,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Favorite)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Favorite).where.eq(column.id, entity.id) }.update.apply()
  }

}
