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
      .configure(
        Map(
          "db.default.fixtures.test" -> List(
            "account.sql",
            "creator.sql",
            "character.sql",
            "world.sql",
            "status.sql"
          )))
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

      val request = FakeRequest(POST, "/world/1/entry/testCharacter1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
      val controller = new WorldController(stubControllerComponents(), authAction)

      val result = call(controller.entry(1, "testCharacter1"), request)

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

    "創作者のワールド一覧確認" in {
      val request = FakeRequest(GET, "/creator/hoge/world")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.getByCreatorId("hoge"), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("hoge")

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
      val request = FakeRequest(POST, "/world/1/entry/hogegege")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.entry(900, "hogegege"), request)

      // ステータスコード404が返ってくる
      status(result) mustBe NOT_FOUND

    }

    "創作者のワールド一覧確認" in {
      val request = FakeRequest(GET, "/creator/inaiyo/worlds")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.getByCreatorId("inaiyo"), request)

      status(result) mustBe NOT_FOUND
    }

    "ワールド取得 NotFound" in {
      val request = FakeRequest(GET, "/world/:id")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.find(999), request)

      status(result) mustBe NOT_FOUND
    }

  }

}
