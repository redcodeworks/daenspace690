package edu.gmu.daenspace690

import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Logger, Level}


object Main {
  def main(): Unit =
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

  val spark = SparkSession.builder()
    .appName("DaenSpace690")
    .master(sys.env.getOrElse("SPARK_MASTER_URL", "local[*]"))
    .getOrCreate()

  import spark.implicits._

  val df = List("hello", "world").toDF

  println(
    spark.sparkContext.parallelize(1 to 100).map(x => x * x).filter(_ % 2 == 0).collect().mkString("Array(", ", ", ")")
  )

  df.show()

  spark.stop()
}