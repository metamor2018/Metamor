package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class StatusControllerSpec extends ControllerSpecBase {

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure(Map("db.default.fixtures.test" -> List(
        "account.sql",
        "creator.sql",
        "character.sql",
        "world.sql",
        "entry.sql",
        "status.sql"
      )))
      .build()

  "success" should {
    "投稿作成" in {
      val request = FakeRequest(POST, "/character/testCharacter1/world/1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"reply": false, "inReplyToId": null, "text": "ほげふがてきすと"}"""))

      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.create("testCharacter1", 1), request)

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("1")
      contentAsString(result) must include("ほげふがてきすと")
      contentAsString(result) must include("testCharacter1")
      contentAsString(result) must include("testName1")
    }

    "投稿取得" in {
      val request = FakeRequest(GET, "/character/hoge/world/1")
      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.get(1, 1), request)

      status(result) mustBe OK
      contentAsString(result) must include("1てきすと1")
      contentAsString(result) must include("1てきすと2")
      contentAsString(result) must include("1てきすと3")
      contentAsString(result) must include("1てきすと4")
      contentAsString(result) must include("1てきすと5")
      contentAsString(result) must include("1てきすと6")
      contentAsString(result) must include("1てきすと7")
      contentAsString(result) must include("1てきすと8")
      contentAsString(result) must include("1てきすと9")
      contentAsString(result) must include("1てきすと10")
      contentAsString(result) must include("1てきすと11")
      contentAsString(result) must include("1てきすと12")
      contentAsString(result) must include("1てきすと13")
      contentAsString(result) must include("1てきすと14")
      contentAsString(result) must include("1てきすと15")
      contentAsString(result) must include("1てきすと16")
      contentAsString(result) must include("1てきすと17")
      contentAsString(result) must include("1てきすと18")
      contentAsString(result) must include("1てきすと19")
      contentAsString(result) must include("1てきすと20")

      contentAsString(result) mustNot include("2てきすと1")
      contentAsString(result) mustNot include("2てきすと3")
      contentAsString(result) mustNot include("2てきすと5")
    }

    "キャラクター別投稿一覧取得" in {
      val request = FakeRequest(GET, "/character/hoge")
      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.getByCharacterId(1, "testCharacter1"), request)

      status(result) mustBe OK
      contentAsString(result) must include("1てきすと1")
      contentAsString(result) must include("1てきすと2")
      contentAsString(result) must include("1てきすと3")
      contentAsString(result) must include("1てきすと4")
      contentAsString(result) must include("1てきすと5")

      contentAsString(result) mustNot include("2てきすと1")
      contentAsString(result) mustNot include("2てきすと3")
      contentAsString(result) mustNot include("2てきすと5")

    }

    "指定個所から最新までの投稿取得" in {
      val request = FakeRequest(GET, "/status/10")
      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.getToLast(10), request)

      status(result) mustBe OK

    }
  }

  "errors" should {
    "投稿作成 バリデーションエラー" in {
      val request = FakeRequest(POST, "/character/testCharacter1/world/1")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"reply": true, "inReplyToId": 999, "text": ""}"""))

      val controller = new StatusController(stubControllerComponents(), authAction)
      val result = call(controller.create("testCharacter1", 1), request)
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
