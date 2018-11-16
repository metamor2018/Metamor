package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import akka.stream.Materializer
import mocks.{ MixInErrorCreatorService, MixInMockCreatorService }
import models.service.CreatorService

class CreatorControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  "success" should {
    "創作者作成" in {
      val request = FakeRequest(POST, "/creator")
        .withJsonBody(Json.parse("""{"displayId": "huga", "name": "ほげ"}"""))
      val controller = new CreatorController(stubControllerComponents())
      with MixInMockCreatorService {
        override val creatorService: CreatorService = mockCreatorService
      }

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }
  }

  "error" should {
    "創作者作成" in {
      val request = FakeRequest(POST, "/creator")
        .withJsonBody(Json.parse("""{"displayId": "huga", "name": "ほげ"}"""))
      val controller = new CreatorController(stubControllerComponents())
      with MixInErrorCreatorService {
        override val creatorService: CreatorService = mockCreatorService
      }
      val result = call(controller.create(), request)
      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ng")
    }
  }

}
