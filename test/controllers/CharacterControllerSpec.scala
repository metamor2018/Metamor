package controllers

import mocks.{ MixInErrorCharacterService, MixInMockCharacterService }
import models.service.CharacterService
import org.scalatest.fixture.FlatSpec
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback

trait CharacterAutoRollback extends FlatSpec with AutoRollback {

  override def fixture(implicit session: DBSession) {
    sql"""
          insert into accounts (auth_id) values ('hoge')
      """.update().apply()

    sql"""
          insert into creators(id,account_id,name)
          values ('huge',1,'huga')
      """.update().apply()

    sql"""
          insert into characters(id,creator_id,name)
          values ('character','huge','huga')
      """.update().apply()
  }

  def errorCharacterCreate(implicit session: DBSession) {
    sql"""
          insert into characters(id,creator_id,name)
          values ('presentcharacter','huge','huga')
      """.update().apply()
  }
}

class CharacterControllerSpec extends PlaySpec with GuiceOneAppPerSuite with ControllerSpecBase {
  "success" should {
    "キャラクター作成" in new CharacterAutoRollback {
      DB autoCommit { implicit session =>
        fixture
      }
      val request = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{ "id": "1","creatorId": "huge", "name": "ほげ"}"""))
      val controller = new CharacterController(stubControllerComponents(), authAction)

      val result = call(controller.create(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

    "キャラクター削除" in {

      val request = FakeRequest(DELETE, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"id": "character"}"""))
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
        .withJsonBody(
          Json.parse("""{"id":"character", "name": "ほげ","profile":"私はほげです","icon":"hogetter"}"""))

      val controller = new CharacterController(stubControllerComponents(), authAction)
      val result = call(controller.edit(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("ok")
    }

  }

  "error" should {
    "キャラクター作成" in new CharacterAutoRollback {
      DB autoCommit { implicit session =>
        errorCharacterCreate
      }
      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInErrorCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }

      val reqest = FakeRequest(POST, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(
          Json.parse("""{ "id": "presentcharacter","creatorId": "oppai", "name": ""}"""))

      val result = call(controller.create(), reqest)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("存在するキャラクターです")
      contentAsString(result) must include("存在しない創作者です")
      contentAsString(result) must include("名前が短すぎます")

    }

    "キャラクター削除" in {
      val controller = new CharacterController(stubControllerComponents(), authAction)
      with MixInErrorCharacterService {
        override val characterService: CharacterService = mockCharacterService
      }

      val request = FakeRequest(DELETE, "/character")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))
        .withJsonBody(Json.parse("""{"id": "noneid"}"""))
      val result = call(controller.delete(), request)

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("存在しないキャラクターです")

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
