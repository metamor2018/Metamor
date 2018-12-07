package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import akka.stream.Materializer
import auth.{ AuthAction, AuthService }
import com.typesafe.config.ConfigFactory
import mocks.{ MixInErrorWorldService, MixInMockWorldService }
import models.service.WorldService
import play.api.mvc.BodyParsers
import play.api.{ Configuration, Environment }
import scalikejdbc._

trait TestDBSettings {

  def loadJDBCSettings() {
    val config = ConfigFactory.load()
    val url = config.getString("jdbc.url")
    val user = config.getString("jdbc.user")
    val password = config.getString("jdbc.password")
    ConnectionPool.singleton(url, user, password)
  }

  loadJDBCSettings()
}

//trait AutoRollbackWithFixture extends AutoRollback {
//
//  override def fixture(implicit session: DBSession) {
//
//    sql"""
//            insert into accounts (auth_id) values (hoge)
//        """.update().apply()
//
//    sql"""
//           insert into creators(display_id,name)
//           values (hoge,huga)
//        """.update().apply()
//
//    sql"""
//           insert into worlds(name,creator_Id,detail,started_at)
//           values ("name",1,"hoge","2018-12-04 06:45:55")
//        """.update().apply()
//  }
//
//}

trait Hoge {
  import play.api.db.{ Database, Databases }
  import play.api.db.evolutions._

  def withMyDatabase[T](block: Database => T) = {
    Databases.withInMemory(
      urlOptions = Map(
        "MODE" -> "MYSQL"
      ),
      config = Map(
        "logStatements" -> true
      )
    ) { database =>
      Evolutions.withEvolutions(database, SimpleEvolutionsReader.forDefault()) {
        block(database)
      }
    }
  }
}

class WorldControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  implicit val ec = stubControllerComponents().executionContext
  val config = Configuration.load(Environment.simple())
  val authAction =
    new AuthAction(
      new BodyParsers.Default,
      new AuthService(config)
    )

  "success" should {
    "ワールド作成" in {
      val request = FakeRequest(POST, "/world")
        .withJsonBody(Json.parse(
          """{"name": "testname", "creatorId": "7", "detail": "テスト作成", "startedAt": "2018-04-01T00:00:00.000+09:00[Asia/Tokyo]"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)
      with MixInMockWorldService {
        override val worldService: WorldService = mockWorldService
      }

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

    "ワールド一覧取得" in {
      val controller = new WorldController(stubControllerComponents(), authAction)
      with MixInMockWorldService {
        override val worldService: WorldService = mockWorldService
      }
      val result = controller.getWorlds().apply(FakeRequest(GET, "/world"))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("testName")
      contentAsString(result) must include("testName2")

    }

    "ワールド参加" in {

      DB autoCommit { implicit session =>
        sql"""
            insert into accounts (`auth_id`) values ("hoge")
        """.update().apply()
//
//        sql"""
//            insert into creators(account_id,display_id,name)
//            values (1,`displayhoge`,`huga`)
//          """.execute().apply()
//
//        sql"""
//            insert into worlds(name,creator_Id,detail,started_at)
//            values ("name",1,"hoge","2018-12-04 06:45:55")
//          """.execute().apply()
      }

      val request = FakeRequest(POST, "/world/entry")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"characterId": "1", "worldId": "2"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)
      with MixInMockWorldService {
        override val worldService: WorldService = mockWorldService
      }

      val result = call(controller.entry(), request)

      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
    }

  }

  "error" should {
    "ワールド作成" in {
      val request = FakeRequest(POST, "/world")
        .withJsonBody(Json.parse(
          """{"name": "testname", "creatorId": "7", "detail": "テスト作成", "startedAt": "2018-04-01T00:00:00.000+09:00[Asia/Tokyo]"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)
      with MixInErrorWorldService {
        override val worldService: WorldService = mockWorldService
      }
      val result = call(controller.create(), request)
      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ng")
    }
  }

}
