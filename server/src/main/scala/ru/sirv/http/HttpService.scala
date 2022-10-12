package ru.sirv.http

import cats.effect.IO
import cats.syntax.all._
import com.typesafe.scalalogging.Logger
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.impl.IntVar
import org.http4s.dsl.io._
import org.http4s.server.Router
import ru.sirv.service._

class HttpService(service: UserService)(implicit logger: Logger){
  def helloWorldService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      logger.info("Received request")
      Ok(s"Hello, $name.")
  }
  def userService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "users" => Ok("GET TEST")
      //service.getUser(_).onError { e =>
      //  logger.error(e.getMessage).pure[IO]
      //}.flatMap(Ok(_))
    case GET -> Root / "users" / IntVar(userId) => Ok(s"GET by id=$userId")
      service.getUser(userId).flatMap(Ok(_))
    case GET -> Root / "test" =>
      Ok("testing message")
    case POST -> Root / "users" => Ok("POST TEST")
      //req.decodeJson[Tweet].flatMap(tweet => Ok(tweet))
    case PUT -> Root / "users" / IntVar(userId) => Ok(s"PUT by id=$userId")
      //logger.info(userId.toString)
      //req.decodeJson[Tweet].flatMap(tweet => Ok(tweet))
    case DELETE -> Root / "users" / IntVar(userId) =>
      Ok(s"DELETE TEST id=$userId")
  }
  def services = userService <+> helloWorldService
  def httpApp = Router("/" -> helloWorldService, "/api" -> services).orNotFound
}