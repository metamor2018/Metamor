package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import mocks.MixInMockCreatorService
import models.service.CreatorService
import scalikejdbc._

class CreatorControllerSpec extends ControllerSpecBase {

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure(Map("db.default.fixtures.test" -> List("account.sql", "creator.sql")))
      .build()

  "success" should {

    "創作者取得" in {

      val request = FakeRequest(GET, "/creator/hoge")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.find("hoge"), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("hoge")
      contentAsString(result) must include("huga")

    }

    "創作者取得 存在しない場合" in {

      val request = FakeRequest(GET, "/creator/inaiyo")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.find("inaiyo"), request)

      status(result) mustBe NOT_FOUND
    }

    "ログインしている創作者の取得" in {
      val request = FakeRequest(GET, "/login/creator")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.findLoginCreator(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("hoge")
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

    "創作者が存在するか確認 存在する場合" in {

      val request = FakeRequest(GET, "/creator/validate/exists")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.exists(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("true")
    }

    "創作者が存在するか確認 存在しない場合" in {

      // 存在しない場合をテストしたいので創作者を削除する
      DB autoCommit { implicit s =>
        sql"""
              DELETE FROM creators;
           """.update().apply()
      }

      val request = FakeRequest(GET, "/creator/validate/exists")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.exists(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("false")
    }

    "創作者作成" in {

      // 創作者が存在すると作成できないため削除
      DB autoCommit { implicit s =>
        sql"""
              DELETE FROM creators;
           """.update().apply()
      }

      val request = FakeRequest(POST, "/creator")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"id": "huga", "name": "ほげ"}"""))

      val controller = new CreatorController(stubControllerComponents(), authAction)

      val result = call(controller.create(), request)

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("creatorId")
      contentAsString(result) must include("huga")
    }

  }
}
