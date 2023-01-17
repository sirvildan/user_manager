package ru.sirv.db

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all._
import org.typelevel.log4cats.slf4j._
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import ru.sirv.conf.ConfigResource
import ru.sirv.db.DbModule.Config
import ru.sirv.domain.{UserMeta, Userinfo}
import ru.sirv.service.UserService

import scala.concurrent.duration.DurationInt
import scala.util.Random

object PopulateDB extends IOApp {
  implicit val loggerCats: SelfAwareStructuredLogger[IO] = LoggerFactory.getLoggerFromName[IO]("PopulateScript")

  val dbConfig: Config = Config(DbModule.PostgresConfig(DbModule.ConnectionConfig("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/sirvildan_db", "postgres", "internet"), DbModule.PoolConfig(8, 1, 3.seconds, Some(1.minute), Some(5.minutes), 8)), "classpath:/migrations/postgres")

  def generateUsers: Seq[Userinfo] =
    (1 to 10000).map { i =>
      //Userinfo("testmail@mail.com", "testname", 3)
      Userinfo(i.toString + "@mail.ru", Random.alphanumeric.take(10).mkString, Random.nextInt(50))
    }

  def generateMetas(users: Seq[Userinfo]): Seq[UserMeta] =
    users.map { ui =>
      UserMeta(ui.email, Random.alphanumeric.take(10).mkString, List.empty)
    }

  def populate(service: UserService): IO[Unit] = {
    val users = generateUsers
    val metas = generateMetas(users)

    users.traverse_(service.addUser) >> metas.traverse_(service.addUserMeta)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val program = for {
      config <- ConfigResource.extractConfig
      _ <- Resource.eval(new DbModule().migrate(config.db.postgres.connection, Seq(config.db.migrationLocation)))
      dbService <- new DbModule().buildService(config.db.postgres.connection, config.db.postgres.commonPool, "PoolName")
      userService = new UserService(dbService)
      _ <- Resource.eval(populate(userService))
    } yield ()

    program.use_.as(ExitCode.Success)
  }
}
