package es.hablapps.auth.domain

package object auth {

  final case class RegistrationError(msg: String)

  final case class AuthnError(msg: String)

  final case class GetError(msg: String)
}
