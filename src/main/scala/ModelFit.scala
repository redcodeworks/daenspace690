package edu.gmu.daenspace690

import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.functions.col

object ModelFit {
  val config: Config = ConfigFactory.load()
  val logLevel = Level.toLevel(config.getString("spark.logLevel"))
  val appName = config.getString("spark.appName")
  val csvUrl = config.getString("input-data.url")
  val documentCol = config.getString("document")
  val labelCol = config.getString("label")


  Logger.getLogger("org.apache.spark").setLevel(logLevel)

  def setSparkSession() =
    SparkSession.builder()
      .appName(appName)
      .master(sys.env.getOrElse("SPARK_MASTER_URL", "local[*]"))
      .getOrCreate()

  def getPipeline(name: String) = name match {
    case "use" => UsePipeline
    case "glove" => GlovePipeline
    case "bert" => BertPipeline
    case "elmo" => ElmoPipeline
    case _ => throw NoSuchElementException
  }

  def main(args: Array[String]): Unit = {
    val spark = setSparkSession()
    import spark.implicits._

    val df = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv(csvUrl)
      .select(col(documentCol).alias("description"), col(labelCol).alias("label"))

    df.show()

    val pipeline = getPipeline(config.getString("pipeline.name"))

    spark.stop()
  }
}