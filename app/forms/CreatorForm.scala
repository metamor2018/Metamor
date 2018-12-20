package forms

import forms.validations.CreatorValidations
import scalaz.Scalaz._
import scalaz._

case class CreatorForm(id: String, name: String) {

  def validate(): Validation[NonEmptyList[String], CreatorForm] = {
    (
      CreatorValidations.displayId(this.id) |@|
        CreatorValidations.name(this.name)
    )(CreatorForm)
  }

}

case class CreatorEditForm(id: Long,
                           displayId: String,
                           name: String,
                           profile: String,
                           icon: String) {

  def validate(): Validation[NonEmptyList[String], CreatorEditForm] = {
    (
      CreatorValidations.id(this.id) |@|
        CreatorValidations.displayId(this.displayId) |@|
        CreatorValidations.name(this.name) |@|
        CreatorValidations.profile(this.profile) |@|
        CreatorValidations.icon(this.icon)
    )(CreatorEditForm)
  }
}
