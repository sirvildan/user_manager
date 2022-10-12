package ru.sirv.http

import cats.effect.IO
import com.typesafe.scalalogging.Logger
import org.http4s.HttpApp
import ru.sirv.service.UserService

trait HttpModule {
  def service: HttpService
  def httpApp: HttpApp[IO]
}

object HttpModule {
  def apply(userService: UserService)(implicit logger: Logger): HttpModule = new HttpModule {
    override def service: HttpService = new HttpService(userService)

    override def httpApp: HttpApp[IO] = service.httpApp
  }
}
