ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"
val sparkVersion = "3.2.3"

lazy val root = (project in file("."))
  .settings(
    name := "daenspace690",
    idePackagePrefix := Some("edu.gmu.daenspace690")
  )

scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")

libraryDependencies ++= Seq(
  ("com.typesafe" % "config" % "1.4.2"),
  ("org.apache.spark" %% "spark-core" % sparkVersion),
  ("org.apache.spark" %% "spark-sql" % sparkVersion),
  ("org.apache.spark" %% "spark-mllib" % sparkVersion),
  ("org.apache.hadoop" % "hadoop-aws" % sparkVersion),
  ("com.johnsnowlabs.nlp" %% "spark-nlp" % "4.3.1"),
  ("com.amazonaws" % "aws-java-sdk" % "1.12.415"),
)

