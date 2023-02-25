package edu.gmu.daenspace690

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label

case class Instruction(procedureName: String, index: Int, start: Int, end: Int, label: Label, text: String)

object Instruction {
  private val parenthesisPattern = "\\(([^\\)]+)\\)".r

  def apply(line: List[String]): Instruction = {
    new Instruction(
      line(0),
      line(1).toInt,
      line(2).toInt,
      line(3).toInt,
      enumLabel(line(4)),
      line(5)
    )
  }

  private def enumLabel(s: String): Label = {
    val step = parenthesisPattern.replaceFirstIn(s, "").trim
    val element = Element.withName(
      parenthesisPattern.findFirstIn(s).map(x => x.substring(1, x.length - 1)).getOrElse("").trim.capitalize
    )

    step match {
      case "actor" => Actor("actor")
      case "trigger" => Trigger(element)
      case "action" => Action(element)
      case "decision" => Decision(element)
      case "waiting" | "wait" => Waiting(element)
      case "verification" | "verify" => Verification(element)
    }



  }

}
