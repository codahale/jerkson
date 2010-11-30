package com.codahale.jerkson

object AST {
  sealed trait JValue

  case object JNull extends JValue

  case class JBoolean(value: Boolean) extends JValue

  case class JInt(value: BigInt) extends JValue

  case class JFloat(value: Double) extends JValue

  case class JString(value: String) extends JValue

  case class JArray(elements: List[JValue]) extends JValue

  case class JField(name: String, value: JValue) extends JValue

  case class JObject(fields: List[JField]) extends JValue
}
