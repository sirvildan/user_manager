package ru.sirv.domain

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

final case class User(email: String, name: String, age: Option[Int])

object User {
  def apply(email: String, name: String, age: Option[Int]): User = new User(email, name, age)

  def apply(email: String, name: String, age: Int): User = new User(email, name, Option(age))

  def apply(email: String, name: String): User = new User(email, name, Option.empty[Int])

  //def isEmailValid(email:String): Boolean = email contains "@"
  def isEmailValid(email: String): Boolean = email.contains("@")

  implicit val userCEncoder: Encoder[User] = deriveEncoder[User]

  implicit val userCDecoder: Decoder[User] = deriveDecoder[User]

  implicit def userEncoder: EntityEncoder[IO, User] = jsonEncoderOf[IO, User]

  implicit def usersDecoder: EntityDecoder[IO, User] = jsonOf[IO, User]
}