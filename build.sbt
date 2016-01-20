name := "contractcalc"

version := "3.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
//  "org.webjars" % "bootstrap" % "3.3.6",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.4.0-M3" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.webjars" % "jquery" % "2.1.4"
)

// resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
