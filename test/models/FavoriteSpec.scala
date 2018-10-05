package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class FavoriteSpec extends Specification {

  "Favorite" should {

    val f = Favorite.syntax("f")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Favorite.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Favorite.findBy(sqls.eq(f.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Favorite.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Favorite.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Favorite.findAllBy(sqls.eq(f.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Favorite.countBy(sqls.eq(f.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Favorite.create(userId = "MyString", contentId = 123, createdAt = null, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Favorite.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Favorite.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Favorite.findAll().head
      val deleted = Favorite.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Favorite.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Favorite.findAll()
      entities.foreach(e => Favorite.destroy(e))
      val batchInserted = Favorite.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
