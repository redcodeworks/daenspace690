ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.12.17"

lazy val root = (project in file("."))
  .settings(
    name := "daenspace690",
    idePackagePrefix := Some("edu.gmu.daenspace690")
  )

scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")


// publish to github packages settings
val githubOwner = "redcodeworks"
val githubRepository = "daenspace690"

publishTo := Some(s"GitHub $githubOwner Apache Maven Packages" at s"https://maven.pkg.github.com/$githubOwner/$githubRepository")
publishMavenStyle := true
credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  githubOwner,
  System.getenv("GITHUB_TOKEN")
)


/** The "provided" tag will prevent certain dependencies from being built into the fat jar.
    The AWS EMR runtime provides most of these already.
    For local development, this "provided" may need to be temporarily removed to allow proper downloading.
 */
val sparkVersion = "3.2.3"
val hadoopVersion = "3.2.3"

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

// needed for assembling fat jars
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _                        => MergeStrategy.first
}

