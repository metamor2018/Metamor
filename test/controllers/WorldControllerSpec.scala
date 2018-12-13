package controllers

import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import mocks.{ MixInErrorWorldService, MixInMockWorldService }
import models.service.WorldService
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.fixture.FlatSpec

trait AutoRollbackWithFixture extends FlatSpec with AutoRollback {

  override def fixture(implicit session: DBSession) {
    sql"""
          insert into accounts (auth_id) values ('hoge')
      """.update().apply()

    sql"""
          insert into creators(account_id,display_id,name)
          values (1,'hoge','huga')
      """.update().apply()

    sql"""
          insert into characters(creator_id,display_id,name)
          values (1,'hoge','huga')
      """.update().apply()

    sql"""
          insert into worlds(name,creator_Id,detail,started_at)
          values ('name',1,'hoge','2018-12-04 06:45:55')
      """.update().apply()
  }
}

class WorldControllerSpec extends ControllerSpecBase {

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

    "ワールド参加" in new AutoRollbackWithFixture {
      DB autoCommit { implicit session =>
        fixture
      }

      val request = FakeRequest(POST, "/world/entry")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"characterId": "1", "worldId": "1"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)

      val result = call(controller.entry(), request)

      // ステータスコード200がかえってくる
      status(result) mustBe OK
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

    "ワールド参加" in {
      // 存在しないキャラクター、ワールドのIDを渡す
      val request = FakeRequest(POST, "/world/entry")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"characterId": "900", "worldId": "900"}"""))

      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.entry(), request)

      // ステータスコード400が返ってくる
      status(result) mustBe BAD_REQUEST
      // jsonが返ってくる
      contentType(result) mustBe Some("application/json")

      // validationエラーが返ってくる
      contentAsString(result) must include("存在しないキャラクターです")
      contentAsString(result) must include("存在しないワールドです")
    }

  }

}
