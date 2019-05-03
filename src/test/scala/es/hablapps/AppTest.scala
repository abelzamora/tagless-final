package es.hablapps

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import cats.implicits._
import es.hablapps.shared.domain.{EmailAddress, User}
import es.hablapps.Main._
import es.hablapps.auth.domain.auth.GetError

import scala.language.higherKinds

class AppTest extends Specification {

  trait Context extends Scope {
    val email = shapeless.tag[EmailAddress]("john@doe.com")
    val password = "swordfish"
    val user = User(email, password)
  }

  "register and authenticate user using a List" in new Context {

    import es.hablapps.auth.DslList._

    val userRepositoryState = registerAndLogin[UserRepositoryState]
    val result = userRepositoryState.runEmpty
    val (users, authenticated) = result.value

    users mustEqual List(user)
  }

  "show an error if user doew not exists" in new Context {
    import es.hablapps.auth.DslList._

    val userRepositoryState2 = getUser[UserRepositoryState](email)
    val result = userRepositoryState2.runEmpty
    val (users, filterUser) = result.value

    users mustEqual List.empty
    filterUser must beLeft(GetError("User not found"))
  }

  "register and authenticate user using a Stream" in new Context {

    import es.hablapps.auth.DslStream._

    val userRepositoryState = registerAndLogin[UserRepositoryStateStream]
    val result = userRepositoryState.runEmpty
    val (users, authenticated) = result.value

    
    users mustEqual Stream[User](user)
  }

}
