package controllers

import akka.stream.Materializer
import auth.AuthAction
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._
import auth.AuthService
import mocks.MixInMockAccountService
import models.service.AccountService
import play.api.{ Configuration, Environment }
import play.api.mvc._

class AccountControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  implicit val ec = stubControllerComponents().executionContext
  val config = Configuration.load(Environment.simple())
  val authAction =
    new AuthAction(
      new BodyParsers.Default,
      new AuthService(config)
    )

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
  }
}
