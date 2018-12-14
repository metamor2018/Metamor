package controllers

import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import mocks.MixInMockCreatorService
import models.service.CreatorService

import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.fixture.FlatSpec

class CreatorControllerSpec extends ControllerSpecBase {

  trait AutoRollbackWithFixture extends FlatSpec with AutoRollback {

    val authId = config.get[String]("auth0.subject")

    override def fixture(implicit session: DBSession) {
      sql"""
            insert into accounts (auth_id) values (${authId})
      """.update().apply()

    }
  }

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

    // DBのロールバック方法がわからないので「存在する場合」より先に検証
    "創作者が存在するか確認 存在しない場合" in new AutoRollbackWithFixture {
      DB autoCommit { implicit session =>
        fixture
      }

      val request = FakeRequest(GET, "/creator/validate/exists")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.exists(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("false")
    }

    "創作者が存在するか確認 存在する場合" in new AutoRollbackWithFixture {

      override def fixture(implicit session: FixtureParam): Unit = {
        sql"""
            insert into accounts (auth_id) values (${authId})
          """.update().apply()

        sql"""
            insert into creators(account_id,display_id,name)
            values (1,'hoge','huga')
        """.update().apply()

      }

      DB autoCommit { implicit session =>
        fixture
      }

      val request = FakeRequest(GET, "/creator/validate/exists")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new CreatorController(stubControllerComponents(), authAction)
      val result = call(controller.exists(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("true")
    }

  }
}
