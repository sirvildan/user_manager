package ru.sirv.db

import ru.sirv.db.DbModule.JdbcDatabaseConfig

import java.sql.{Connection, DriverManager}
import scala.concurrent.Future

class DbModule(config: JdbcDatabaseConfig) {
  def migrate: Future[Boolean] = DBMigrations.migrate(config)

  def setup: Future[DbService] = ???

  def getConnection(): Connection = {
    try {
      val con: Connection = DriverManager
        .getConnection("jdbc:postgresql://localhost:5432/sirvildan_db",
          "postgres",
          "internet")

      con
    }
  }
}

object DbModule {
  final case class JdbcDatabaseConfig(
                                       url: String = "jdbc:postgresql://localhost/sirvildan_db",
                                       driver: String = "com.mysql.cj.jdbc.Driver",
                                       user: String = "postgres",
                                       password: String = "internet",
                                       migrationsTable: String = "flyway_table",
                                       migrationsLocations: List[String] = List("classpath:migrations/postgres")
                                     )
}