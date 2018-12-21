package controllers

import akka.stream.Materializer
import mocks.{ MixInErrorCharacterService, MixInMockCharacterService }
import models.service.CharacterService
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class CharacterControllerSpec extends PlaySpec with GuiceOneAppPerSuite with ControllerSpecBase {
  "success" should {
    "キャラクター作成" in {
      val request = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{ "Id": "testid","creatorId": "1", "name": "ほげ"}"""))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInMockCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

    "キャラクター削除" in {
      val request = FakeRequest(DELETE, "/characterDelete")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"Id": "1"}"""))
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
      val request = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{ "Id": "testid","creatorId": "1", "name": "ほげ"}"""))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInErrorCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }
      val result = call(controller.create(), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ng")
    }

    "キャラクター削除" in {
      val request = FakeRequest(DELETE, "/characterDelete")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"Id": "0"}"""))
      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInErrorCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }
      val result = call(controller.delete(), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ng")
    }
  }

}
