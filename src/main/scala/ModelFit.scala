package edu.gmu.daenspace690

import com.typesafe.config.{Config, ConfigFactory}
import Spark.session.implicits._


object ModelFit {

  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load()
    val pipelineName = config.getString("pipeline.name")
    val saveFittedAs = Seq(config.getString("pipeline.save-fitted-loc"), pipelineName, ".model").mkString
    val saveResultsAs = Seq(config.getString("pipeline.save-predictions-loc"), pipelineName, ".predictions.parquet").mkString

    val trainDf = ProjectData.trainingSet
    val testDf = ProjectData.testSet

    trainDf.show()
    testDf.show()

    val pl = pipelineName match {
      case "use"   => UsePipeline
      case "glove" => GlovePipeline
      case "bert"  => BertPipeline
      case "elmo"  => ElmoPipeline
      case _ =>
        throw new java.util.NoSuchElementException(
          "Not a valid pipeline name. Check README for available options."
        )
    }

    val fittedModel = pl.pipeline.fit(trainDf)
    val predictions = fittedModel.transform(testDf)

    if (config.getBoolean("pipeline.save-predictions")) {
      println(s"Saving predictions as ${saveResultsAs}")
      predictions.toDF().write.parquet(saveResultsAs)
    } else predictions.show()

    if (config.getBoolean("pipeline.save-fitted")) {
      println(s"Saving fitted model as ${saveFittedAs}")
      fittedModel.write.overwrite().save(saveFittedAs)
    }

    Spark.session.stop()
  }
}
