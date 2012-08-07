package com.codahale.jerkson.tests

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonIgnore, JsonProperty, JsonCreator, JsonValue}
import com.codahale.jerkson.{JsonSnakeCase,JsonNoJerkson}

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

@JsonNoJerkson
case class NoJerksonCaseClass(id: Int, name: String) {
  @JsonCreator def this(
    @JsonProperty("ID") id: Option[Int],
    @JsonProperty("name-thing") name: Option[String],
    @JsonProperty("meta") meta: Any
  ) = this(id.get, name.get)

  @JsonValue def toJson = Map(
    "ID" -> id,
    "name-thing" -> name,
    "meta" -> Seq(id, name)
  )
}

@JsonNoJerkson
class NoJerksonIterator(var count: Int) extends Iterator[Int] {
  def hasNext = true
  def next = { count += 1; count - 1 }

  @JsonCreator def this(
    @JsonProperty("count") count: Option[Int]
  ) = this(count.get)

  @JsonValue def toJson = Map(
    "count" -> count
  )
}
