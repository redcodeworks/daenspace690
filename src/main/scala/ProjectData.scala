package edu.gmu.daenspace690

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.log4j.{Level, Logger}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.functions._
import Spark.session.implicits._


object ProjectData {
  val config: Config = ConfigFactory.load()
  val csvUrl = config.getString("input-data.url")
  val documentCol = config.getString("input-data.document")
  val labelCol = config.getString("input-data.label")

  val sourceDf: DataFrame = Spark.session.read
    .option("header", true)
    .option("inferSchema", true)
    .csv(csvUrl)
    .cache()

  val df: DataFrame = sourceDf
    .select(sourceDf.columns.map(x => col(x).as(x.toLowerCase)): _*)
    .select(
      $"procedure name",
        expr(
    """
                stack(
                    16,
                    'actor', actor,
                    'trigger (what)', `trigger (what)`,
                    'trigger (how)', `trigger (how)`,
                    'trigger (where)', `trigger (where)`,
                    'decision (what)', `decision (what)`,
                    'decision (how)', `decision (how)`,
                    'decision (where)', `decision (where)`,
                    'action (what)', `action (what)`,
                    'action (how)', `action (how)`,
                    'action (where)', `action (where)`,
                    'waiting (what)', `waiting (what)`,
                    'waiting (how)', `waiting (how)`,
                    'waiting (where)', `waiting (where)`,
                    'verification (what)', `verification (what)`,
                    'verification (how)', `verification (how)`,
                    'verification (where)', `verification (where)`
                ) as (label, text)
            """)
        )
    .filter("text is not null")

  val weights = Array(
    config.getDouble("input-data.train-split"),
    1 - config.getDouble("input-data.train-split")
  )

  val splits = df.randomSplit(weights)

  val trainingSet = splits(0)
  val testSet = splits(1)


  trainingSet.show(20, false)
  testSet.show(20, false)

}
