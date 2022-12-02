package ru.sirv

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.typesafe.scalalogging.Logger
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j._
import ru.sirv.conf.ConfigResource
import ru.sirv.db.DbModule
import ru.sirv.http.HttpModule
import ru.sirv.service.UserService

object Main extends IOApp {
  implicit val loggerCats: SelfAwareStructuredLogger[IO] = org.typelevel.log4cats.LoggerFactory.getLoggerFromName[IO]("Main")
  implicit val loggerTS: Logger = com.typesafe.scalalogging.Logger("Main") // TODO remove
  def run(args: List[String]): IO[ExitCode] = {
    val program = for {
      config <- ConfigResource.extractConfig
      _ <- Resource.eval(new DbModule().migrate(config.db.postgres.connection, Seq(config.db.migrationLocation)))
      dbService <- new DbModule().buildService(config.db.postgres.connection, config.db.postgres.commonPool, "PoolName")
      userService = new UserService(dbService)
      httpModule = HttpModule(userService, config.http)
      server <- httpModule.service.server
    } yield server

    program.useForever.as(ExitCode.Success)
  }
}