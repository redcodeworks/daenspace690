ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"
val sparkVersion = "3.2.3"
val hadoopVersion = "3.2.3"

githubOwner := "redcodeworks"
githubRepository := "daenspace690"

lazy val root = (project in file("."))
  .settings(
    name := "daenspace690",
    idePackagePrefix := Some("edu.gmu.daenspace690")
  )

scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")

/** The "provided" tag will prevent certain dependencies from being built into the fat jar.
    The AWS EMR runtime provides most of these already.
    For local development, this "provided" may need to be temporarily removed to allow proper downloading.
 */
libraryDependencies ++= Seq(
  ("com.typesafe" % "config" % "1.4.2"),
  ("org.apache.spark" %% "spark-core" % sparkVersion),
  ("org.apache.spark" %% "spark-sql" % sparkVersion ),
  ("org.apache.spark" %% "spark-mllib" % sparkVersion),
  ("org.apache.hadoop" % "hadoop-client" % hadoopVersion),
  ("org.apache.hadoop" % "hadoop-aws" % hadoopVersion),
  ("com.amazonaws" % "aws-java-sdk" % "1.12.415"),
  ("com.johnsnowlabs.nlp" %% "spark-nlp" % "4.3.1"),
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _                        => MergeStrategy.first
}

