import Versions._
import sbt._

object Dependencies {
//  val test = Seq(
//    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
//  )

  val cats = Seq(
    "org.typelevel" %% "cats-core" % Versions.Cats,
  )

  val catsEffect = Seq(
    "org.typelevel" %% "cats-effect" % Versions.CatsEffect,
  )

  val IntTest = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % "test",
    "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersScalaVersion % "test"
  )

  val database = Seq(
    "org.postgresql" % "postgresql" % postgresqlVersion,
    "org.flywaydb" % "flyway-core" % flywayVersion
  )

  val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.17.1"
  )
  val logging = Seq(
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion
  )
  val lihaoyi = Seq(
    "com.lihaoyi" %% "requests" % lihaoyiVersion
    //"com.lihaoyi" %% "requests" % lihaoyi_sec
  )
  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-ember-server" % http4sVersion,
    "org.http4s" %% "http4s-ember-client" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion
  )

  val circe = Seq(
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core"      % doobieVersion,
    "org.tpolecat" %% "doobie-h2"        % doobieVersion,          // H2 driver 1.4.200 + type mappings.
    "org.tpolecat" %% "doobie-hikari"    % doobieVersion,          // HikariCP transactor.
    "org.tpolecat" %% "doobie-postgres"  % doobieVersion,          // Postgres driver 42.3.1 + type mappings.
    "org.tpolecat" %% "doobie-specs2"    % doobieVersion % "test", // Specs2 support for typechecking statements.
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test",
    "io.estatico"  %% "newtype"         % NewTypeVersion
  )

  val log4Cats = Seq(
    "org.typelevel" %% "log4cats-slf4j" % "2.3.0",
    "org.typelevel" %% "log4cats-noop"  % "2.3.0",
  )

  val lib = Def.setting(
    IntTest ++ database ++ lihaoyi ++ http4s ++ circe ++ doobie ++ pureconfig ++ logging ++ log4Cats
  )
}

object Versions {
  val Cats               = "2.9.0"
  val CatsEffect         = "3.4.5"
  val scalaTestVersion = "3.2.12"
  val postgresqlVersion = "42.5.1"
  val flywayVersion = "8.4.0"
  val lihaoyiVersion = "0.6.9"
  //val lihaoyi_sec = "0.7.0"
  val logbackVersion = "1.2.3"
  val http4sVersion = "1.0.0-M39"
  val circeVersion = "0.14.1"
  val doobieVersion = "1.0.0-RC1"
  val NewTypeVersion = "0.4.4"
  val testcontainersScalaVersion = "0.40.0"
}
