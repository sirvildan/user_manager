//import Dependencies._
import sbt.Compile
import sbt.Keys.compile
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val IntegrationTest = config("it").extend(Test)

lazy val root = (project in file("."))
  .aggregate(server, core)
  .dependsOn(server)
  .configs(IntegrationTest)
  .settings(
    inConfig(IntegrationTest)(Defaults.testSettings),
    libraryDependencies ++= Dependencies.lib.value,
    Compile / run / fork      := true,
    Compile / run / mainClass := Some("ru.sirv.Main"),
    Test / fork := true
  )

lazy val server = (project in file("server"))
  .dependsOn(core)
  .configs(IntegrationTest)
  .settings(
    inConfig(IntegrationTest)(Defaults.testSettings),
    name := "server",
    libraryDependencies ++= Dependencies.lib.value,
    compile / exportJars := true
  )

lazy val core = (project in file ("core"))
    .settings(
    name := "core",
    libraryDependencies ++= Dependencies.lib.value
)
