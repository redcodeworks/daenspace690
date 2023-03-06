package edu.gmu.daenspace690

trait Label
case class Actor(name: String) extends Label
case class Trigger(ele: Element.Value) extends Label
case class Action(ele: Element.Value) extends Label
case class Decision(ele: Element.Value) extends Label
case class Waiting(ele: Element.Value) extends Label
case class Verification(ele: Element.Value) extends Label

object Element extends Enumeration {
  type Element = Value
  val What, Where, How = Value
}
