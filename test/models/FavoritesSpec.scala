package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class FavoritesSpec extends Specification {

  "Favorites" should {

    val f = Favorites.syntax("f")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Favorites.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Favorites.findBy(sqls.eq(f.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Favorites.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Favorites.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Favorites.findAllBy(sqls.eq(f.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Favorites.countBy(sqls.eq(f.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Favorites.create(userId = "MyString", contentId = 123, createdAt = null, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Favorites.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Favorites.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Favorites.findAll().head
      val deleted = Favorites.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Favorites.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Favorites.findAll()
      entities.foreach(e => Favorites.destroy(e))
      val batchInserted = Favorites.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
