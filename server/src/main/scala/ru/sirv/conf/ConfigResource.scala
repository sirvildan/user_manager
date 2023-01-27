package ru.sirv.conf

import cats.syntax.all._
import cats.effect.{ IO, Resource }
import pureconfig.error.ConfigReaderException
import pureconfig.{ ConfigReader, ConfigSource }
import ru.sirv.db.DbModule
import ru.sirv.http.HttpModule

object ConfigResource {
  import Config._

  def extractConfig: Resource[IO, Config] =
    (extractDbConfig, extractHttpConfig).mapN(Config(_, _))

  def extractHttpConfig: Resource[IO, HttpModule.Config] =
    extract[HttpModule.Config]("http")

  def extractDbConfig: Resource[IO, DbModule.Config] =
    extract[DbModule.Config]("db")

  private def extract[A: ConfigReader](namespace: String): Resource[IO, A] =
    Resource.eval(
      ConfigSource.default
        .at(namespace)
        .load[A]
        .toValidated
        .fold(error => IO.raiseError(ConfigReaderException(error)), a => IO.pure(a))
    )
}
