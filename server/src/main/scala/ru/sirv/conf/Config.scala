package ru.sirv.conf

import pureconfig._
import pureconfig.generic.semiauto._
import ru.sirv.db.DbModule
import ru.sirv.db.DbModule.{ ConnectionConfig, PoolConfig, PostgresConfig }
import ru.sirv.http.{ HttpModule, HttpService }

case class Config(db: DbModule.Config, http: HttpModule.Config)

object Config {
  implicit val connectionConfigReader: ConfigReader[ConnectionConfig]    = deriveReader[ConnectionConfig]
  implicit val poolConfigReader: ConfigReader[PoolConfig]                = deriveReader[PoolConfig]
  implicit val postgresConfigReader: ConfigReader[PostgresConfig]        = deriveReader[PostgresConfig]
  implicit val dbModuleConfigReader: ConfigReader[DbModule.Config]       = deriveReader[DbModule.Config]
  implicit val httpConfigReader: ConfigReader[HttpModule.Config]         = deriveReader[HttpModule.Config]
  implicit val httpServiceConfigReader: ConfigReader[HttpService.Config] = deriveReader[HttpService.Config]
  implicit val ConfigReader: ConfigReader[Config]                        = deriveReader[Config]
}
