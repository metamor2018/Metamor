package models

import scalikejdbc._
import java.time.{ ZonedDateTime }

case class Relation(id: Int,
                    followId: String,
                    followerId: String,
                    createdAt: ZonedDateTime,
                    updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = Relation.autoSession): Relation =
    Relation.save(this)(session)

  def destroy()(implicit session: DBSession = Relation.autoSession): Int =
    Relation.destroy(this)(session)

}

object Relation extends SQLSyntaxSupport[Relation] {

  override val tableName = "relations"

  override val columns = Seq("id", "follow_id", "follower_id", "created_at", "updated_at")

  def apply(r: SyntaxProvider[Relation])(rs: WrappedResultSet): Relation = apply(r.resultName)(rs)
  def apply(r: ResultName[Relation])(rs: WrappedResultSet): Relation = new Relation(
    id = rs.get(r.id),
    followId = rs.get(r.followId),
    followerId = rs.get(r.followerId),
    createdAt = rs.get(r.createdAt),
    updatedAt = rs.get(r.updatedAt)
  )

  val r = Relation.syntax("r")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Relation] = {
    withSQL {
      select.from(Relation as r).where.eq(r.id, id)
    }.map(Relation(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Relation] = {
    withSQL(select.from(Relation as r)).map(Relation(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Relation as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Relation] = {
    withSQL {
      select.from(Relation as r).where.append(where)
    }.map(Relation(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Relation] = {
    withSQL {
      select.from(Relation as r).where.append(where)
    }.map(Relation(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Relation as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(followId: String,
             followerId: String,
             createdAt: ZonedDateTime,
             updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): Relation = {
    val generatedKey = withSQL {
      insert
        .into(Relation)
        .namedValues(
          column.followId -> followId,
          column.followerId -> followerId,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    Relation(
      id = generatedKey.toInt,
      followId = followId,
      followerId = followerId,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(entities: collection.Seq[Relation])(
      implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(
      entity =>
        Seq(
          'followId -> entity.followId,
          'followerId -> entity.followerId,
          'createdAt -> entity.createdAt,
          'updatedAt -> entity.updatedAt
      ))
    SQL("""insert into relations(
      follow_id,
      follower_id,
      created_at,
      updated_at
    ) values (
      {followId},
      {followerId},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Relation)(implicit session: DBSession = autoSession): Relation = {
    withSQL {
      update(Relation)
        .set(
          column.id -> entity.id,
          column.followId -> entity.followId,
          column.followerId -> entity.followerId,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Relation)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Relation).where.eq(column.id, entity.id) }.update.apply()
  }

}
