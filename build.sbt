name := "lunchroom-ms"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

val reactiveMongoVer = "0.16.0-play26"

libraryDependencies ++= Seq(
  guice,
  "org.reactivemongo"      %% "play2-reactivemongo" % reactiveMongoVer,

  "org.scalatestplus.play" %% "scalatestplus-play"  % "3.1.1" % Test
)

import play.sbt.routes.RoutesKeys

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"
