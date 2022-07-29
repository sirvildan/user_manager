import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "user_manager",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "test",
    libraryDependencies += "org.postgresql" % "postgresql" % "42.4.0",
      libraryDependencies += "org.flywaydb" % "flyway-core" % "8.4.0",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

  )

