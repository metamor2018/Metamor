name := """metamor"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  evolutions,
  "mysql" % "mysql-connector-java" % "6.0.5",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.3",
  "com.dripower" %% "play-circe" % "2610.0"
)

scalafmtConfig := Some(file(".scalafmt.conf"))
scalafmtOnCompile := true