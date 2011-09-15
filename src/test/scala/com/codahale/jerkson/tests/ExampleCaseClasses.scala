package com.codahale.jerkson.tests

import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.annotate.{JsonIgnoreProperties, JsonIgnore}

case class CaseClass(id: Long, name: String)

@JsonIgnoreProperties(Array("uncomfortable", "unpleasant"))
case class CaseClassWithIgnoredFields(id: Long,
                                      uncomfortable: String = "Bad Touch",
                                      unpleasant: String = "The Creeps")

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
