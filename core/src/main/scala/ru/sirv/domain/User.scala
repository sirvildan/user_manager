package ru.sirv.domain

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

final case class Userinfo(email: String, name: String, age: Option[Int])

object Userinfo {
  def apply(email: String, name: String, age: Option[Int]): Userinfo = new Userinfo(email, name, age)

  def apply(email: String, name: String, age: Int): Userinfo = new Userinfo(email, name, Option(age))

  def apply(email: String, name: String): Userinfo = new Userinfo(email, name, Option.empty[Int])

  def isEmailValid(email: String): Boolean = email.contains("@")

  implicit val userCEncoder: Encoder[Userinfo] = deriveEncoder[Userinfo]

  implicit val userCDecoder: Decoder[Userinfo] = deriveDecoder[Userinfo]

  implicit def userEncoder: EntityEncoder[IO, Userinfo] = jsonEncoderOf[Userinfo]

  implicit def usersDecoder: EntityDecoder[IO, Userinfo] = jsonOf[IO, Userinfo]
}