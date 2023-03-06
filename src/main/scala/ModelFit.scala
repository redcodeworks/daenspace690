package edu.gmu.daenspace690

import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.functions.col

object ModelFit {

  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load()

    val pipeline = config.getString("pipeline.name") match {
      case "use"   => UsePipeline
      case "glove" => GlovePipeline
      case "bert"  => BertPipeline
      case "elmo"  => ElmoPipeline
      case _ =>
        throw new java.util.NoSuchElementException(
          "Not a valid pipeline name. Check README for available options."
        )
    }

    val trainDf = ProjectData.trainingSet
    val testDf = ProjectData.testSet

    trainDf.show()
    testDf.show()

    Spark.session.stop()
  }
}
