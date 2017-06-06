name := """scala-test-task"""
organization := "com.sysgears"

version := "0.1"
scalaVersion := "2.11.11"

libraryDependencies ++= List(
  filters,
  "org.reactivemongo" % "play2-reactivemongo_2.11" % "0.12.3",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)