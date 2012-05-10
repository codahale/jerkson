package com.codahale.jerkson.tests

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonIgnore}
import com.codahale.jerkson.JsonSnakeCase

case class CaseClass(id: Long, name: String)

case class CaseClassWithLazyVal(id: Long) {
  lazy val woo = "yeah"
}

case class CaseClassWithIgnoredField(id: Long) {
  @JsonIgnore
  val uncomfortable = "Bad Touch"
}

@JsonIgnoreProperties(Array("uncomfortable", "unpleasant"))
case class CaseClassWithIgnoredFields(id: Long) {
  val uncomfortable = "Bad Touch"
  val unpleasant = "The Creeps"
}

case class CaseClassWithTransientField(id: Long) {
  @transient
  val lol = "I'm sure it's just a phase."
}

case class CaseClassWithOverloadedField(id: Long) {
  def id(prefix: String): String = prefix + id
}

case class CaseClassWithOption(value: Option[String])

case class CaseClassWithJsonNode(value: JsonNode)

case class CaseClassWithAllTypes(map: Map[String, String],
                                 set: Set[Int],
                                 string: String,
                                 list: List[Int],
                                 seq: Seq[Int],
                                 indexedSeq: IndexedSeq[Int],
                                 vector: Vector[Int],
                                 bigDecimal: BigDecimal,
                                 bigInt: BigInt,
                                 int: Int,
                                 long: Long,
                                 char: Char,
                                 bool: Boolean,
                                 short: Short,
                                 byte: Byte,
                                 float: Float,
                                 double: Double,
                                 any: Any,
                                 anyRef: AnyRef,
                                 intMap: Map[Int, Int],
                                 longMap: Map[Long, Long])

object OuterObject {
  case class NestedCaseClass(id: Long)

  object InnerObject {
    case class SuperNestedCaseClass(id: Long)
  }
}

case class CaseClassWithTwoConstructors(id: Long,  name: String) {
  def this(id: Long) = this(id,  "New User")
}

@JsonSnakeCase
case class CaseClassWithSnakeCase(oneThing: String, twoThing: String)

case class CaseClassWithArrays(one: String, two: Array[String], three: Array[Int])
