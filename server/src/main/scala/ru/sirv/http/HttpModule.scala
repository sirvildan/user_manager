package ru.sirv.http

import cats.effect.IO
import org.http4s.HttpApp
import org.typelevel.log4cats.Logger
import ru.sirv.service.UserService

trait HttpModule {
  def service: HttpService
  def httpApp: HttpApp[IO]
}

object HttpModule {
  case class Config(server: HttpService.Config)
  def apply(userService: UserService, config: Config)(implicit logger: Logger[IO]): HttpModule = new HttpModule {
    override def service: HttpService = new HttpService(userService, config.server)

    override def httpApp: HttpApp[IO] = service.httpApp
  }
}
