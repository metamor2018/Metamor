package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class ContentsSpec extends Specification {

  "Contents" should {

    val c = Contents.syntax("c")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Contents.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Contents.findBy(sqls.eq(c.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Contents.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Contents.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Contents.findAllBy(sqls.eq(c.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Contents.countBy(sqls.eq(c.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Contents.create(userId = "MyString", content = "MyString", createdAt = null, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Contents.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Contents.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Contents.findAll().head
      val deleted = Contents.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Contents.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Contents.findAll()
      entities.foreach(e => Contents.destroy(e))
      val batchInserted = Contents.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
