package es.hablapps

import cats.Monad
import cats.implicits._
import es.hablapps.auth.domain.auth.AuthnError
import es.hablapps.auth.{Dsl => AuthnDsl}
import es.hablapps.shared.domain.{EmailAddress, User}

import shapeless.tag

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

    import es.hablapps.auth.DslList._

    val userRepositoryState = registerAndLogin[UserRepositoryState]
    val result = userRepositoryState.runEmpty
    val (users, authenticated) = result.value

    println(s"Authenticated: $authenticated")
    println(s"Registered users: $users")

    import es.hablapps.auth.DslStream._

    val userRepositoryStateStream = registerAndLogin[UserRepositoryStateStream]
    val resultStream = userRepositoryStateStream.runEmpty
    val (usersStream, authenticatedStream) = resultStream.value

    println(s"AuthenticatedStream: $authenticated")
    println(s"Registered users Stream: $usersStream")

}
