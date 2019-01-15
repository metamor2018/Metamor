package controllers

import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import mocks.MixInMockWorldService
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
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(
          Json.parse("""{"name": "testname","creatorId": "hoge",  "detail": "テスト作成"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)

      val result = call(controller.create(), request)

      status(result) mustBe CREATED
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
        .withJsonBody(Json.parse("""{"characterId": "testCharacter1", "worldId": "1"}"""))
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

    "創作者のワールド一覧確認" in {
      val request = FakeRequest(GET, "/creator/hoge/world")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.getByCreatorId("hoge"), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("hoge")

    }

    "キャラクターが参加しているワールド一覧取得" in {
      val request = FakeRequest(GET, "/character/testCharacter1/world")
      val controller = new WorldController(stubControllerComponents(), authAction)
      val result = call(controller.getByCharacterId("testCharacter1"), request)

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
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(
          Json.parse("""{"name": "nonetestname","creatorId": "nonehoge",  "detail": "テスト作成"}"""))
      val controller = new WorldController(stubControllerComponents(), authAction)

      val result = call(controller.create(), request)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) must include("存在しない創作者です")

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
