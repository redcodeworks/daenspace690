package edu.gmu.daenspace690

import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.functions.col

object Spark {
  val config: Config = ConfigFactory.load()
  val logLevel = Level.toLevel(config.getString("spark.logLevel"))
  val appName = config.getString("spark.appName")

  val session = SparkSession.builder()
    .appName(config.getString("spark.appName"))
    .master(sys.env.getOrElse("SPARK_MASTER_URL", "local[*]"))
    .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
    .getOrCreate()

  session.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", sys.env.getOrElse("AWS_ACCESS_KEY_ID", ""))
  session.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", ""))
  session.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

  Logger.getLogger("org.apache.spark").setLevel(logLevel)
}
