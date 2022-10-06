import Versions._
import sbt._

object Dependencies {
  val test = Seq(
    "org.scalatest" %% "scalatest" % scalaTest % "test"
  )
  val database = Seq(
    "org.postgresql" % "postgresql" % postgresql,
    "org.flywaydb" % "flyway-core" % flyway
  )
  val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLogging
  )
  val lihaoyi = Seq(
    "com.lihaoyi" %% "requests" % "0.6.9",
    //"com.lihaoyi" %% "requests" % lihaoyi_sec
  )

  val lib = Def.setting(
    test ++ database ++ logging ++ lihaoyi
  )
}

object Versions {
  val scalaTest = "3.2.12"
  val postgresql = "42.4.0"
  val flyway = "8.4.0"
  val scalaLogging = "3.9.5"
  val lihaoyi = "0.6.9"
  //val lihaoyi_sec = "0.7.0"
}