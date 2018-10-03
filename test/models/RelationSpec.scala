package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class RelationSpec extends Specification {

  "Relation" should {

    val r = Relation.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Relation.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Relation.findBy(sqls.eq(r.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Relation.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Relation.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Relation.findAllBy(sqls.eq(r.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Relation.countBy(sqls.eq(r.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Relation.create(followId = "MyString", followerId = "MyString", createdAt = null, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Relation.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Relation.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Relation.findAll().head
      val deleted = Relation.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Relation.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Relation.findAll()
      entities.foreach(e => Relation.destroy(e))
      val batchInserted = Relation.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
