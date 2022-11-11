package ru.sirv.http

import cats.effect.{IO, Resource}
import cats.syntax.all._
import com.comcast.ip4s.{Host, Port}
import com.typesafe.scalalogging.Logger
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.toMessageSyntax
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.{Router, Server}
import ru.sirv.service._
import ru.sirv.domain._
import ru.sirv.http.HttpService.Config

class HttpService(service: UserService, config: Config)(implicit logger: Logger){
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
      req.decodeJson[Userinfo].flatMap(userinfo => service.addUser(userinfo)) *> Created()
    case req@PUT -> Root / "users" / UUIDVar(userId) =>
      req.decodeJson[Userinfo].flatMap(userinfo => service.updateUser(userinfo, userId)) *> Ok()
    case DELETE -> Root / "users" / UUIDVar(userId) =>
      service.deleteUser(userId) *> Ok()

    case GET -> Root / "usermeta" =>
      service.getAllMeta.flatMap(Ok(_))
    case GET -> Root / "usermeta" / UUIDVar(usermetaId) =>
      service.getUserMeta(usermetaId).flatMap(Ok(_))
    case req@POST -> Root / "usermeta" =>
      req.decodeJson[UserMeta].flatMap(userMeta => service.addUserMeta(userMeta)) *> Created()
    case req@PUT -> Root / "usermeta" / UUIDVar(usermetaId) =>
      req.decodeJson[UserMeta].flatMap(userMeta => service.updateUserMeta(userMeta, usermetaId)) *> Ok()
    case DELETE -> Root / "usermeta" / UUIDVar(usermetaId) =>
      service.deleteUserMeta(usermetaId) *> Ok()
  }
  def services = userService <+> helloWorldService
  def httpApp = Router("/" -> helloWorldService, "/api" -> services).orNotFound
  def server: Resource[IO, Server] = EmberServerBuilder
    .default[IO]
    .withHost(Host.fromString(config.host).get)
    .withPort(Port.fromInt(config.port).get)
    .withHttpApp(httpApp)
    .build

}

object HttpService{
  case class Config(host: String, port: Int)
}