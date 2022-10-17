import Versions._
import sbt._

object Dependencies {
  val test = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )
  val database = Seq(
    "org.postgresql" % "postgresql" % postgresqlVersion,
    "org.flywaydb" % "flyway-core" % flywayVersion
  )
  val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
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
    "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test"
  )

  val lib = Def.setting(
    test ++ database ++ logging ++ lihaoyi ++ http4s ++ circe ++ doobie
  )
}

object Versions {
  val scalaTestVersion = "3.2.12"
  val postgresqlVersion = "42.4.0"
  val flywayVersion = "8.4.0"
  val scalaLoggingVersion = "3.9.5"
  val lihaoyiVersion = "0.6.9"
  //val lihaoyi_sec = "0.7.0"
  val logbackVersion = "1.2.3"
  val http4sVersion = "0.23.12"
  val circeVersion = "0.14.1"
  val doobieVersion = "1.0.0-RC1"
}
