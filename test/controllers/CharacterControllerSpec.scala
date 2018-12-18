package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import akka.stream.Materializer
import auth.{ AuthAction, AuthService }
import mocks.{ MixInErrorCharacterService, MixInMockCharacterService }
import models.service.CharacterService
import play.api.mvc.BodyParsers
import play.api.{ Configuration, Environment }

class CharacterControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  implicit val ec = stubControllerComponents().executionContext

  val config: Configuration = Configuration.load(Environment.simple())
  val authAction =
    new AuthAction(
      new BodyParsers.Default,
      new AuthService(config)
    )

  "success" should {
    "キャラクター作成" in {
      val request = FakeRequest(POST, "/character")
        .withJsonBody(Json.parse("""{"creatorId": "1", "displayId": "test", "name": "ほげ"}"""))
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

    "キャラクター編集" in {
      val request = FakeRequest(PUT, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse(
          """{"id":"1","displayId": "huga", "name": "ほげ","profile":"私はほげです","icon":"hogetter"}"""))

      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInMockCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }
      val result = call(controller.edit(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

  }

  "error" should {
    "キャラクター作成" in {
      val request = FakeRequest(POST, "/character")
        .withJsonBody(Json.parse("""{"creatorId": "1", "displayId": "test", "name": "ほげ"}"""))
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
