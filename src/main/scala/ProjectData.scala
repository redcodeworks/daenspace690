package edu.gmu.daenspace690

import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.functions.col

object ProjectData {
  val config: Config = ConfigFactory.load()
  val csvUrl = config.getString("input-data.url")
  val documentCol = config.getString("input-data.document")
  val labelCol = config.getString("input-data.label")

  val df = Spark.session.read
    .option("header", true)
    .option("inferSchema", true)
    .csv(csvUrl)
    .select(col(documentCol).alias("description"), col(labelCol).alias("label"))
    .cache()

  val weights = Array(
    config.getDouble("input-data.train-split"),
    1 - config.getDouble("input-data.train-split")
  )

  val splits = df.randomSplit(weights)

  val trainingSet = splits(0)
  val testSet = splits(1)

}
