name := "contractcalc"

version := "3.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.webjars" %% "webjars-play" % "2.2.1-2",
  "org.webjars" % "jquery" % "1.11.1"
)

play.Project.playScalaSettings
