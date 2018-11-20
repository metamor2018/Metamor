package controllers

import akka.stream.Materializer
import mocks.{ MixInErrorCharacterService, MixInMockCharacterService }
import models.service.CharacterService
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class CharacterControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  "success" should {
    "キャラクター作成" in {
      val request = FakeRequest(POST, "/character")
        .withJsonBody(Json.parse("""{"creatorId": "1", "displayId": "test", "name": "ほげ"}"""))
      val controller = new CharacterController(stubControllerComponents())
      with MixInMockCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }
  }

  "error" should {
    "キャラクター作成" in {
      val request = FakeRequest(POST, "/character")
        .withJsonBody(Json.parse("""{"creatorId": "1", "displayId": "test", "name": "ほげ"}"""))
      val controller = new CharacterController(stubControllerComponents())
      with MixInErrorCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }
      val result = call(controller.create(), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ng")
    }
  }

}
