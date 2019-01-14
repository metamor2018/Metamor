package controllers

import akka.stream.Materializer
import auth.{ AuthAction, AuthService }
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{ Configuration, Environment }
import play.api.mvc.BodyParsers
import play.api.test.Helpers.stubControllerComponents

/**
  * テストの事前準備をするトレイト
  */
trait ControllerSpecBase extends PlaySpec with GuiceOneAppPerSuite {
  implicit lazy val materializer: Materializer = app.materializer

  implicit val ec = stubControllerComponents().executionContext

  val config = Configuration.load(Environment.simple())

  val authAction =
    new AuthAction(
      new BodyParsers.Default,
      new AuthService(config)
    )
}
