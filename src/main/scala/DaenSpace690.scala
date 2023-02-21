package edu.gmu.daenspace690

import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Logger, Level}


object Main {

  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

  val spark = SparkSession.builder()
    .appName("DaenSpace690")
    .master(sys.env.getOrElse("SPARK_MASTER_URL", "local[*]"))
    .getOrCreate()

  import spark.implicits._

  val csvData = getClass.getResource("/airlineSops.csv")

  def main(args: Array[String]): Unit = {

    val df = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv(csvData.getPath)

    df.show()

    spark.stop()
  }
}