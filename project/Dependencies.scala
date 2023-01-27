import Versions._
import sbt._

object Dependencies {
  val cats = Seq(
    "org.typelevel" %% "cats-core" % Versions.Cats
  )

  val catsEffect = Seq(
    "org.typelevel" %% "cats-effect" % Versions.CatsEffect
  )

  val circe = Seq(
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )

  val database = Seq(
    "org.postgresql" % "postgresql" % postgresqlVersion,
    "org.flywaydb" % "flyway-core" % flywayVersion
  )

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-h2" % doobieVersion, // H2 driver 1.4.200 + type mappings.
    "org.tpolecat" %% "doobie-hikari" % doobieVersion, // HikariCP transactor.
    "org.tpolecat" %% "doobie-postgres" % doobieVersion, // Postgres driver 42.3.1 + type mappings.
    "org.tpolecat" %% "doobie-specs2" % doobieVersion % "test", // Specs2 support for typechecking statements.
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test",
    "io.estatico" %% "newtype" % NewTypeVersion
  )

  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-ember-server" % http4sVersion,
    "org.http4s" %% "http4s-ember-client" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )

  val lihaoyi = Seq(
    "com.lihaoyi" %% "requests" % lihaoyiVersion
  )

  val log4Cats = Seq(
    "org.typelevel" %% "log4cats-slf4j" % logforcatsVersion,
    "org.typelevel" %% "log4cats-noop" % logforcatsVersion,
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureconfigVersion
  )

  val tests = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % "test",
    "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersScalaVersion % "test"
  )

  val lib = Def.setting(
    circe ++ database ++ doobie ++ http4s ++ lihaoyi ++ log4Cats ++ logging ++ pureconfig ++ tests
  )
}

object Versions {
  val Cats = "2.9.0"
  val CatsEffect = "3.4.5"
  val circeVersion = "0.15.0-M1"
  val doobieVersion = "1.0.0-RC2"
  val flywayVersion = "8.4.0"
  val http4sVersion = "1.0.0-M39"
  val lihaoyiVersion = "0.8.0"
  val logbackVersion = "1.4.5"
  val logforcatsVersion = "2.5.0"
  val NewTypeVersion = "0.4.4"
  val postgresqlVersion = "42.5.1"
  val pureconfigVersion = "0.17.2"
  val scalaTestVersion = "3.2.15"
  val testcontainersScalaVersion = "0.40.12"
}