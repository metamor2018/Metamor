package forms

import forms.validations.CreatorValidations
import scalaz.Scalaz._
import scalaz._

case class CreatorForm(displayId: String, name: String) {

  def validate(): Validation[NonEmptyList[String], CreatorForm] = {
    (
      CreatorValidations.displayId(this.displayId) |@|
        CreatorValidations.name(this.name)
    )(CreatorForm)
  }

}
