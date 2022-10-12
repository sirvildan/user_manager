import settings._
import Dependencies._
import sbt.Compile
import sbt.Keys.compile
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .aggregate(server, core)
  .dependsOn(server)
  .settings(
      Compile / run / fork      := true,
      Compile / run / mainClass := Some("ru.sirv.Main")
  )

lazy val server = (project in file("server"))
  .dependsOn(core)
  .settings(
    name := "manager_server",
    commonSettings2_13(),
    libraryDependencies ++= Dependencies.lib.value,
    compile / exportJars := true
  )

lazy val core = (project in file ("core"))
    .settings(
    name := "core",
    commonSettings2_13(),
    libraryDependencies ++= Dependencies.lib.value
)