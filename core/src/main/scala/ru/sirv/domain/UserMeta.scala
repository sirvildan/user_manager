package ru.sirv.domain

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

final case class UserMeta(email: String, hobby: String, friendsEmails: List[String])

  object UserMeta {
    implicit val userMetaCEncoder: Encoder[UserMeta] = deriveEncoder[UserMeta]

    implicit val userMetaCDecoder: Decoder[UserMeta] = deriveDecoder[UserMeta]

    implicit def userMetaEncoder: EntityEncoder[IO, UserMeta] = jsonEncoderOf[UserMeta]

    implicit def usersMetaDecoder: EntityDecoder[IO, UserMeta] = jsonOf[IO, UserMeta]
  }