name := """metamor"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  javaJdbc,
  evolutions,
  "mysql" % "mysql-connector-java" % "6.0.5",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,

)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

// sbt scalafmtでコードフォーマット
scalafmtConfig := Some(file(".scalafmt.conf"))
scalafmtOnCompile := true // compile時に自動でコードフォーマット
