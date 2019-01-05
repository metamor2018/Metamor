package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import mocks.{ MixInErrorCharacterService, MixInMockCharacterService }
import models.service.CharacterService
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class CharacterControllerSpec extends ControllerSpecBase {

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure(Map("db.default.fixtures.test" -> List("default.sql", "entry.sql", "status.sql")))
      .build()

  "success" should {
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

  }

  "error" should {
    "キャラクター作成" in {
      val controller = new CharacterController(stubControllerComponents(), authAction)

      val reqest = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{ "id": "hoge","creatorId": "inaiyo", "name": ""}"""))

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
  }

}
