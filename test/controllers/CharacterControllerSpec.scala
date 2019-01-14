package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import mocks.MixInMockCharacterService
import models.service.CharacterService
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class CharacterControllerSpec extends ControllerSpecBase {

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure(
        Map(
          "db.default.fixtures.test" -> List(
            "account.sql",
            "creator.sql",
            "character.sql"
          )))
      .build()

  "success" should {

    "キャラクター取得" in {
      val request = FakeRequest(GET, "/character/testCharacter1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.find("testCharacter1"), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("hoge")
    }

    "キャラクター取得 存在しない場合" in {
      val request = FakeRequest(GET, "/character/inaiyo")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.find("inaiyo"), request)

      status(result) mustBe NOT_FOUND
    }

    "キャラクター作成" in {

      val request = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{ "id": "testchara","creatorId": "hoge", "name": "ほげ"}"""))
      val controller = new CharacterController(stubControllerComponents(), authAction)

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

    "キャラクター削除" in {

      val request = FakeRequest(DELETE, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"id": "testchara"}"""))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInMockCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }
      val result = call(controller.delete(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

    "キャラクター一覧確認1ページ目" in {
      val request = FakeRequest(GET, "/creator/hoge/character/1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.getByCreatorId("hoge", 1), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("testCharacter1")
      contentAsString(result) must include("testCharacter10")
      contentAsString(result) mustNot include("testCharacter11")
    }

    "キャラクター一覧確認 2ページ目" in {
      val request = FakeRequest(GET, "/creator/hoge/character/2")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.getByCreatorId("hoge", 2), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustNot include("testCharacter9")
      contentAsString(result) mustNot include("testCharacter10")
      contentAsString(result) must include("testCharacter11")
      contentAsString(result) must include("testCharacter20")
      contentAsString(result) mustNot include("testCharacter21")

    }
    "キャラクター編集" in {
      val request = FakeRequest(PUT, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse(
          """{"id":"testCharacter1", "name": "ほげ","profile":"私はほげです","icon":"hogetter"}"""))

      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.edit(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

  }

  "error" should {
    "キャラクター作成" in {
      val controller = new CharacterController(stubControllerComponents(), authAction)

      val reqest = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{ "id": "testCharacter1","creatorId": "inaiyo", "name": ""}"""))

      val result = call(controller.create(), reqest)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("存在するキャラクターです")
      contentAsString(result) must include("存在しない創作者です")
      contentAsString(result) must include("名前が短すぎます")

    }

    "キャラクター削除" in {
      val controller = new CharacterController(stubControllerComponents(), authAction)
      val request = FakeRequest(DELETE, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"id": "noneid"}"""))
      val result = call(controller.delete(), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("存在しないキャラクターです")

    }

    "キャラクター一覧確認" in {
      val request = FakeRequest(GET, "/creator/nonhuge/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
      val controller = new CharacterController(stubControllerComponents(), authAction)

      val result = call(controller.getByCreatorId("nonhuge", 999), request)

      status(result) mustBe NOT_FOUND
    }

    "キャラクター編集" in {
      val request = FakeRequest(PUT, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(
          Json.parse("""{"id":"noncharacter", "name": "","profile":"私はほげです","icon":"hogetter"}"""))

      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.edit(), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("存在しないキャラクターです")
      contentAsString(result) must include("名前が短すぎます")
    }
  }

}
