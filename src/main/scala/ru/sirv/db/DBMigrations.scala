package ru.sirv.db

import com.typesafe.scalalogging.LazyLogging
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.Location
import org.flywaydb.core.Flyway
import ru.sirv.db.DbModule.JdbcDatabaseConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters._
import scala.concurrent.ExecutionContext.Implicits.global

object DBMigrations extends LazyLogging {

  def migrate(config: JdbcDatabaseConfig): Future[Boolean] =
    Future {
      logger.info(
        "Running migrations from locations: " +
          config.migrationsLocations.mkString(", ")
      )
      val isSuccess: Boolean = unsafeMigrate(config)
      logger.info(s"Executed $isSuccess migrations")
      isSuccess
    }

  private def unsafeMigrate(config: JdbcDatabaseConfig): Boolean = {
    val m: FluentConfiguration = Flyway.configure
      .dataSource(
        config.url,
        config.user,
        config.password
      )
      .group(true)
      .outOfOrder(false)
      .table(config.migrationsTable)
      .locations(
        config.migrationsLocations
          .map(new Location(_))
          .toList: _*
      )
      .baselineOnMigrate(true)

    logValidationErrorsIfAny(m)
    val migrateResult = m.load().migrate()
    migrateResult.success
  }

  private def logValidationErrorsIfAny(m: FluentConfiguration): Unit = {
    val validated = m.ignorePendingMigrations(true)
      .load()
      .validateWithResult()

    if (!validated.validationSuccessful)
      for (error <- validated.invalidMigrations.asScala)
        logger.warn(
          s"""
             |Failed validation:
             |  - version: ${error.version}
             |  - path: ${error.filepath}
             |  - description: ${error.description}
             |  - errorCode: ${error.errorDetails.errorCode}
             |  - errorMessage: ${error.errorDetails.errorMessage}
        """.stripMargin)
  }
}
