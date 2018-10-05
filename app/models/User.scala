package models

import scalikejdbc._
import java.time.{ ZonedDateTime }

case class User(id: String,
                userId: String,
                name: String,
                createdAt: ZonedDateTime,
                updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Int = User.destroy(this)(session)

}

object User extends SQLSyntaxSupport[User] {

  override val tableName = "users"

  override val columns = Seq("id", "user_id", "name", "created_at", "updated_at")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    id = rs.get(u.id),
    userId = rs.get(u.userId),
    name = rs.get(u.name),
    createdAt = rs.get(u.createdAt),
    updatedAt = rs.get(u.updatedAt)
  )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(id: String)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(User as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(User as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(id: String,
             userId: String,
             name: String,
             createdAt: ZonedDateTime,
             updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): User = {
    withSQL {
      insert
        .into(User)
        .namedValues(
          column.id -> id,
          column.userId -> userId,
          column.name -> name,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.update.apply()

    User(id = id, userId = userId, name = name, createdAt = createdAt, updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[User])(
      implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(
      entity =>
        Seq(
          'id -> entity.id,
          'userId -> entity.userId,
          'name -> entity.name,
          'createdAt -> entity.createdAt,
          'updatedAt -> entity.updatedAt
      ))
    SQL("""insert into users(
      id,
      user_id,
      name,
      created_at,
      updated_at
    ) values (
      {id},
      {userId},
      {name},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.name -> entity.name,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(User).where.eq(column.id, entity.id) }.update.apply()
  }

}
