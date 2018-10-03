package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class RelationsSpec extends Specification {

  "Relations" should {

    val r = Relations.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Relations.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Relations.findBy(sqls.eq(r.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Relations.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Relations.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Relations.findAllBy(sqls.eq(r.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Relations.countBy(sqls.eq(r.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Relations.create(followId = "MyString", followerId = "MyString", createdAt = null, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Relations.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Relations.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Relations.findAll().head
      val deleted = Relations.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Relations.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Relations.findAll()
      entities.foreach(e => Relations.destroy(e))
      val batchInserted = Relations.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
