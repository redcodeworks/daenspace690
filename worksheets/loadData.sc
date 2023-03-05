


val lines = scala.io.Source.fromResource("data/airlineSops.csv").getLines()


import edu.gmu.daenspace690._


val hIdx = lines.next()
  .map(_.toLower)
  .split(",")
  .zipWithIndex
  .toMap

val s = "747 air conditioning packs,747,44728,0.669402777777778,1,364,383,trigger (what),trigger,after engine start:"

Instruction(
  List(0, 4, 5, 6, 7, 9)
    .map(
      s.split(",")
    )
)

lines
  .map(_.toLowerCase)
  .map(ln => List(0, 4, 5, 6, 7, 9).map(
    ln.split(",").map(_.trim).toList))
  .map(ln => Instruction(ln))
  .foreach(println)


