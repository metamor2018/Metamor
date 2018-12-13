package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import akka.stream.Materializer
import auth.{ AuthAction, AuthService }
import mocks. MixInMockCreatorService
import models.service.CreatorService
import play.api.mvc.BodyParsers
import play.api.{ Configuration, Environment }

class CreatorControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  implicit val ec = stubControllerComponents().executionContext

  val config: Configuration = Configuration.load(Environment.simple())
  val authAction =
    new AuthAction(
      new BodyParsers.Default,
      new AuthService(config)
    )

  "success" should {
    "創作者作成" in {
      val request = FakeRequest(POST, "/creator")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"displayId": "huga", "name": "ほげ"}"""))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      with MixInMockCreatorService {
        override val creatorService: CreatorService = mockCreatorService
      }

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

    "創作者編集" in {
      val request = FakeRequest(PUT, "/creator")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse(
          """{"id":"1","displayId": "huga", "name": "ほげ","profile":"私はほげです","icon":"hogetter"}"""))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      with MixInMockCreatorService {
        override val creatorService: CreatorService = mockCreatorService
      }
      val result = call(controller.edit(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }
  }
  

}
