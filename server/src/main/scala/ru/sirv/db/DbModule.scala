package ru.sirv.db

import cats.effect.{IO, Resource}
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import doobie.{ExecutionContexts, Transactor}
import org.typelevel.log4cats.Logger
import ru.sirv.db.DbModule.{ConnectionConfig, PoolConfig}

import scala.concurrent.duration.FiniteDuration

class DbModule {
  def buildService(db: ConnectionConfig, pool: PoolConfig, poolName: String): Resource[IO, DbService[IO]] =
    DbModule.transactor(db, pool, poolName).flatMap{xa =>
      val accessor = new DoobieAccessor
      DbService.impl(xa, accessor)
    }

  def migrate(db: ConnectionConfig, locations: Seq[String])(implicit log: Logger[IO]): IO[Unit] =
    IO.whenA(locations.nonEmpty)(DBMigrations.migrate(db, locations.head, locations.tail:_*).void)
}
object DbModule {
  case class Config(postgres: PostgresConfig, migrationLocation: String)

  case class PostgresConfig(connection: ConnectionConfig, commonPool: PoolConfig)

  final case class ConnectionConfig(
                                     jdbcDriverName: String,
                                     jdbcUrl: String,
                                     user: String,
                                     password: String,
                                   )

  final case class PoolConfig(
                               connectionMaxPoolSize: Int,
                               connectionIdlePoolSize: Int,
                               connectionTimeout: FiniteDuration,
                               connectionIdleTimeout: Option[FiniteDuration],
                               connectionMaxLifetime: Option[FiniteDuration],
                               threadPoolSize: Int,
                             )

  def transactor(db: ConnectionConfig, pool: PoolConfig, poolName: String): Resource[IO, Transactor[IO]] =
    ExecutionContexts.fixedThreadPool[IO](pool.threadPoolSize).flatMap { ec =>
      HikariTransactor.fromHikariConfig[IO](hikariConfig(db, pool, poolName), ec)
    }

  def hikariConfig(db: ConnectionConfig, pool: PoolConfig, poolName: String): HikariConfig = {
    val conf = new HikariConfig()

    conf.setMaximumPoolSize(pool.connectionMaxPoolSize)
    conf.setMinimumIdle(pool.connectionIdlePoolSize)
    conf.setConnectionTimeout(pool.connectionTimeout.toMillis)

    pool.connectionMaxLifetime.foreach { max =>
      conf.setMaxLifetime(max.toMillis)
    }
    pool.connectionIdleTimeout.foreach { idle =>
      conf.setIdleTimeout(idle.toMillis)
    }

    conf.setPoolName(poolName)

    conf.setDriverClassName(db.jdbcDriverName)
    conf.setJdbcUrl(db.jdbcUrl)
    conf.setUsername(db.user)
    conf.setPassword(db.password)

    conf
  }

}