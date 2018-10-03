package models

import scalikejdbc._
import java.time.{ ZonedDateTime }

case class Content(id: Int,
                   userId: String,
                   content: String,
                   createdAt: ZonedDateTime,
                   updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = Content.autoSession): Content =
    Content.save(this)(session)

  def destroy()(implicit session: DBSession = Content.autoSession): Int =
    Content.destroy(this)(session)

}

object Content extends SQLSyntaxSupport[Content] {

  override val tableName = "contents"

  override val columns = Seq("id", "user_id", "content", "created_at", "updated_at")

  def apply(c: SyntaxProvider[Content])(rs: WrappedResultSet): Content = apply(c.resultName)(rs)
  def apply(c: ResultName[Content])(rs: WrappedResultSet): Content = new Content(
    id = rs.get(c.id),
    userId = rs.get(c.userId),
    content = rs.get(c.content),
    createdAt = rs.get(c.createdAt),
    updatedAt = rs.get(c.updatedAt)
  )

  val c = Content.syntax("c")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Content] = {
    withSQL {
      select.from(Content as c).where.eq(c.id, id)
    }.map(Content(c.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Content] = {
    withSQL(select.from(Content as c)).map(Content(c.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Content as c)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Content] = {
    withSQL {
      select.from(Content as c).where.append(where)
    }.map(Content(c.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Content] = {
    withSQL {
      select.from(Content as c).where.append(where)
    }.map(Content(c.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Content as c).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(userId: String, content: String, createdAt: ZonedDateTime, updatedAt: ZonedDateTime)(
      implicit session: DBSession = autoSession): Content = {
    val generatedKey = withSQL {
      insert
        .into(Content)
        .namedValues(
          column.userId -> userId,
          column.content -> content,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    Content(
      id = generatedKey.toInt,
      userId = userId,
      content = content,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(entities: collection.Seq[Content])(
      implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(
      entity =>
        Seq(
          'userId -> entity.userId,
          'content -> entity.content,
          'createdAt -> entity.createdAt,
          'updatedAt -> entity.updatedAt
      ))
    SQL("""insert into contents(
      user_id,
      content,
      created_at,
      updated_at
    ) values (
      {userId},
      {content},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Content)(implicit session: DBSession = autoSession): Content = {
    withSQL {
      update(Content)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.content -> entity.content,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Content)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Content).where.eq(column.id, entity.id) }.update.apply()
  }

}
