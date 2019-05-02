package es.hablapps.auth

import cats.data.State
import cats.implicits._
import es.hablapps.shapelessext._
import es.hablapps.auth.domain.auth.{AuthnError, RegistrationError}
import es.hablapps.shared.domain.{EmailAddress, User}
import shapeless.tag.@@

trait Dsl[F[_]]{

  def register(email: String @@ EmailAddress, password: String):
      F[Either[RegistrationError, User]]

  def authn(email: String @@ EmailAddress, password: String):
      F[Either[AuthnError, User]]
}

object DslList {

  type UserRepository = List[User]
  type UserRepositoryState[A] = State[UserRepository, A]

  implicit object StateInterpreter extends Dsl[UserRepositoryState] {
    override def register(email: String @@ EmailAddress, password: String):
        UserRepositoryState[Either[RegistrationError, User]] =
      State { users =>
        if (users.exists(_.email === email))
          (users, RegistrationError("User already exists").asLeft)
        else{
          val user = User(email, password)
          (users :+ user, user.asRight)
        }
      }

    override def authn(email: String @@ EmailAddress, password: String):
        UserRepositoryState[Either[AuthnError, User]] =
      State.inspect(_
                      .find(user => user.email === email && user.password === password)
                      .toRight(AuthnError("Authentication failed")))
  }
}

object DslStream {
  type UserRepositoryStream = Stream[User]
  type UserRepositoryStateStream[A] = State[UserRepositoryStream, A]

  implicit object StateInterpreterStream extends Dsl[UserRepositoryStateStream] {
    override def register(email: String @@ EmailAddress, password: String):
        UserRepositoryStateStream[Either[RegistrationError, User]] =
      State { users =>
        if(users.exists(_.email === email))
          (users, RegistrationError("User already exists in Stream").asLeft)
        else {
          val user = User(email, password)
          (users :+ user, user.asRight)
        }
      }

    override def authn(email: String @@ EmailAddress, password: String):
        UserRepositoryStateStream[Either[AuthnError, User]] =
      State.inspect(_
                      .find(user => user.email === email && user.password === password)
    .toRight(AuthnError("Authentication failed in Stream")))
  }
}
