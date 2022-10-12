package ru.sirv

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.comcast.ip4s._
import com.typesafe.scalalogging.Logger
import org.http4s.ember.server._
import org.http4s.server.Server
import ru.sirv.http.HttpModule
import ru.sirv.service.UserService

object Main extends IOApp {
  implicit val logger = Logger("Main")

  def run(args: List[String]): IO[ExitCode] = {
    val userService = new UserService()
    val httpModule = HttpModule(userService)

    val server: Resource[IO, Server] = EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpModule.httpApp)
      .build

    server.useForever.as(ExitCode.Success)
  }
}