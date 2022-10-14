package ru.sirv.http

import cats.effect.IO
import cats.syntax.all._
import com.typesafe.scalalogging.Logger
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.toMessageSyntax
import org.http4s.dsl.io._
import org.http4s.server.Router
import ru.sirv.service._
import ru.sirv.domain._

class HttpService(service: UserService)(implicit logger: Logger){
  def helloWorldService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      logger.info("Received request")
      Ok(s"Hello, $name.")
  }
  def userService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "users" =>
      service.getAllUsers.flatMap(Ok(_))
    case GET -> Root / "users" / UUIDVar(userId) =>
      service.getUser(userId).flatMap(Ok(_))
    case req@POST -> Root / "users" =>
      req.decodeJson[User].flatMap(user => service.addUser(user)) *> Created()
    case req@PUT -> Root / "users" / UUIDVar(userId) =>
      req.decodeJson[User].flatMap(user => service.updateUser(user, userId)) *> Ok()
    case DELETE -> Root / "users" / UUIDVar(userId) =>
      service.deleteUser(userId) *> Ok()
  }
  def services = userService <+> helloWorldService
  def httpApp = Router("/" -> helloWorldService, "/api" -> services).orNotFound
}