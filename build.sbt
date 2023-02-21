ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"

lazy val root = (project in file("."))
  .settings(
    name := "daenspace690",
    idePackagePrefix := Some("edu.gmu.daenspace690")
  )

scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")
libraryDependencies ++= Seq(
  ("org.apache.spark" %% "spark-core" % "3.3.2"),
  ("org.apache.spark" %% "spark-sql" % "3.3.2"),
  ("com.johnsnowlabs.nlp" %% "spark-nlp" % "4.3.0" % Test)
)

