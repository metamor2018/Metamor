package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class ContentSpec extends Specification {

  "Content" should {

    val c = Content.syntax("c")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Content.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Content.findBy(sqls.eq(c.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Content.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Content.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Content.findAllBy(sqls.eq(c.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Content.countBy(sqls.eq(c.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Content.create(userId = "MyString", content = "MyString", createdAt = null, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Content.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Content.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Content.findAll().head
      val deleted = Content.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Content.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Content.findAll()
      entities.foreach(e => Content.destroy(e))
      val batchInserted = Content.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
