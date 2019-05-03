package es.hablapps

import cats.Monad
import cats.implicits._
import es.hablapps.auth.domain.auth.{AuthnError, GetError}
import es.hablapps.auth.{Dsl => AuthnDsl}
import es.hablapps.shared.domain.{EmailAddress, User}
import shapeless.tag
import shapeless.tag.@@

import scala.language.higherKinds

object Main extends App {

  def registerAndLogin[F[_]: Monad](implicit authnDsl: AuthnDsl[F]):
      F[Either[AuthnError, User]] = {

    val email = tag[EmailAddress]("john@doe.com")
    val password = "swordfish"

    for {
      _ <- authnDsl.register(email, password)
      authenticated <- authnDsl.authn(email, password)
    } yield authenticated

  }

  def getUser[F[_]: Monad](email: String @@ EmailAddress)(implicit authnDsl: AuthnDsl[F]):
    F[Either[GetError, User]] =
      for {
        user <- authnDsl.getUser(email)
      } yield user


}
