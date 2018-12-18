package controllers

import play.api.test.Helpers._
import play.api.test._
import mocks.MixInMockAccountService

import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.fixture.FlatSpec
import models.service.AccountService

class AccountControllerSpec extends ControllerSpecBase {

  trait AutoRollbackWithFixture extends FlatSpec with AutoRollback {

    val authId = config.get[String]("auth0.subject")

    override def fixture(implicit session: DBSession) {
      sql"""
            insert into accounts (auth_id) values (${authId})
      """.update().apply()

    }
  }

  "success" should {
    "account作成" in {
      val request = FakeRequest(POST, "/account")
        .withHeaders(
          "Authorization" -> ("Bearer " + config.get[String]("auth0.token"))
        )

      val controller = new AccountController(stubControllerComponents(), authAction)
      with MixInMockAccountService {
        override val accountService: AccountService = mockAccountService
      }
      val result = call(controller.signup(), request)

      status(result) mustBe OK
    }

    // DBのロールバック方法がわからないので「存在する場合」より先に検証
    "創作者が存在するか確認 存在しない場合" in new AutoRollbackWithFixture {
      DB autoCommit { implicit session =>
        fixture
      }

      val request = FakeRequest(GET, "/creator/validate/exists")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new AccountController(stubControllerComponents(), authAction)
      val result = call(controller.signup(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("existsCreator")
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

      val request = FakeRequest(POST, "/account")
        .withHeaders("Authorization" -> ("Bearer " + config.get[String]("auth0.token")))

      val controller = new AccountController(stubControllerComponents(), authAction)
      val result = call(controller.signup(), request)

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("existsCreator")
      contentAsString(result) must include("true")
    }

  }
}
