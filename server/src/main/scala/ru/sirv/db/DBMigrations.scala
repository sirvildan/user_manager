package ru.sirv.db

import cats.effect.IO
import cats.syntax.all._
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.exception.FlywayValidateException
import org.flywaydb.core.api.output.{MigrateErrorResult, MigrateResult}
import org.typelevel.log4cats.Logger
import ru.sirv.db.DbModule.ConnectionConfig

import scala.jdk.CollectionConverters._

object DBMigrations {

  def migrate(config: ConnectionConfig, fstLocation: String, rstLocations: String*)(
    implicit log: Logger[IO]
  ): IO[MigrateResult] = {
    val locations = fstLocation +: rstLocations
    log.info(s"Running migrations from locations: [${locations.mkString(", ")}]") *>
      loadFlyway(config, locations)
        .map(_.migrate())
        .flatTap {
          case e: MigrateErrorResult =>
            log.error(
              s"Migration failure after ${e.migrationsExecuted} executed migrations," +
                s" reason: [${e.error.message}] ${e.error.message}."
            )
          case r =>
            log.info(s"Migration success after ${r.migrationsExecuted} executed migrations.")
        }
  }

  def validate(config: ConnectionConfig, fstLocation: String, rstLocations: String*)(
    implicit log: Logger[IO]
  ): IO[Unit] = {
    val locations = fstLocation +: rstLocations
    loadFlyway(config, locations)
      .map(_.validateWithResult())
      .flatMap { result =>
        if (result.validationSuccessful) IO.unit
        else {
          result.invalidMigrations.asScala.toList.traverse_ { error =>
            log.warn(
              s"Migration validation failed for '${error.version}@${error.filepath}':" +
                s" [${error.errorDetails.errorCode}] ${error.errorDetails.errorMessage}"
            )
          } *> IO.raiseError {
            new FlywayValidateException(
              result.errorDetails,
              result.getAllErrorMessages
            )
          }
        }
      }
  }

  def loadFlyway(config: ConnectionConfig, locations: Seq[String]): IO[Flyway] = IO.blocking {
    Flyway.configure
      .dataSource(
        config.jdbcUrl,
        config.user,
        config.password
      )
      .baselineOnMigrate(true)
      .baselineVersion("000")
      .locations(locations: _*)
      .mixed(true)
      .load()
  }
}
