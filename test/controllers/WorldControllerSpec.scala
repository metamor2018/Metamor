package controllers

import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import mocks.{ MixInErrorWorldService, MixInMockWorldService }
import models.service.WorldService

class WorldControllerSpec extends ControllerSpecBase {

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure(Map("db.default.fixtures.test" -> List("default.sql")))
      .build()

  "success" should {
    "ワールド作成" in {
      val request = FakeRequest(POST, "/world")
        .withJsonBody(Json.parse(
          """{"creatorId": "hogeCreator", "name": "testname", "detail": "テスト作成", "startedAt": "2018-04-01T00:00:00.000+09:00[Asia/Tokyo]"}"""))
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

      val request = FakeRequest(POST, "/world/entry")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"characterId": "hoge", "worldId": "1"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)

      val result = call(controller.entry(), request)

      // ステータスコード200がかえってくる
      status(result) mustBe OK
    }

//    "開催中ワールド一覧取得" in {
//      val controller = new WorldController(stubControllerComponents(), authAction)
//      with MixInMockWorldService {
//        override val worldService: WorldService = mockWorldService
//      }
//      val result = controller.getEnable().apply(FakeRequest(GET, "/world"))
//
//      status(result) mustBe OK
//      contentType(result) mustBe Some("application/json")
//      contentAsString(result) must include("testName")
//      contentAsString(result) must include("testName2")
//
//    }

    "他の創作者のワールド確認" in {
      val request = FakeRequest(GET, "/creator/:displayId/worlds")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"creatorId": "hoge"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)
      with MixInMockWorldService {
        override val worldService: WorldService = mockWorldService
      }
      val result = call(controller.getByCreatorId("1"), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("id")

    }

    "ワールド取得" in {
      val request = FakeRequest(GET, "/world/:id")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.find(1), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("hoge")
    }

  }

  "error" should {
    "ワールド作成" in {
      val request = FakeRequest(POST, "/world")
        .withJsonBody(Json.parse(
          """{"creatorId": "hogeCreator", "name": "testname", "detail": "テスト作成", "startedAt": "2018-04-01T00:00:00.000+09:00[Asia/Tokyo]"}"""))
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
        .withJsonBody(Json.parse("""{"characterId": "hogegege", "worldId": "900"}"""))

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

    "他の創作者のワールド確認" in {
      val request = FakeRequest(GET, "/creator/:displayId/worlds")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"creatorId": "1000"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)
      with MixInErrorWorldService {
        override val worldService: WorldService = mockWorldService
      }
      val result = call(controller.getByCreatorId("1"), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("存在しない創作者です")
    }

    "ワールド取得 NotFound" in {
      val request = FakeRequest(GET, "/world/:id")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.find(999), request)

      status(result) mustBe NOT_FOUND
    }

  }

}
