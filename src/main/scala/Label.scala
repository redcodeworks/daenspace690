package edu.gmu.daenspace690

enum Label:
  case Trigger(ele: Element)
  case Action(ele: Element)
  case Decision(ele: Element)
  case Waiting(ele: Element)
  case Verification(ele: Element)

enum Element:
  case What
  case Where
  case How