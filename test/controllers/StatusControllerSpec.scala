package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class StatusControllerSpec extends ControllerSpecBase {

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure(Map("db.default.fixtures.test" -> List("default.sql")))
      .build()

  "success" should {
    "投稿作成" in {
      val request = FakeRequest(POST, "/character/hoge/world/1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"reply": false, "inReplyToId": null, "text": "ほげふがてきすと"}"""))

      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.create("hoge", 1), request)

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("1")
      contentAsString(result) must include("ほげふがてきすと")
    }
  }

  "errors" should {
    "投稿作成 バリデーションエラー" in {
      val request = FakeRequest(POST, "/character/hoge/world/1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"reply": true, "inReplyToId": 999, "text": ""}"""))

      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.create("hoge", 1), request)
      status(result) mustBe BAD_REQUEST
      contentAsString(result) must include("内容がありません")
      contentAsString(result) must include("存在しない投稿です")

    }
  }

  "errors" should {
    "投稿作成 NotFound" in {
      val request = FakeRequest(POST, "/character/naiyo/world/1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"reply": false, "inReplyToId": null, "text": "ほげふがてきすと"}"""))

      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.create("naiyo", 1), request)

      status(result) mustBe NOT_FOUND
    }
  }
}
