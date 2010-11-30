package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.node.IntNode
import com.codahale.jerkson.AST._

object JsonParsingSpec extends Spec {
  class `Parsing a JSON boolean` {
    def `should be readable as a Boolean` {
      parse[Boolean]("true") must beTrue
      parse[Boolean]("false") must beFalse
    }

    def `should be readable as a JValue` {
      parse[JValue]("true") must beEqualTo(JBoolean(true))
      parse[JValue]("false") must beEqualTo(JBoolean(false))
    }
  }

  class `Parsing a JSON int` {
    def `should be readable as an Int` {
      parse[Int]("1") must beEqualTo(1)
    }

    def `should be readable as an Option[Int]` {
      parse[Option[Int]]("1") must beSome(1)
    }

    def `should be readable as a Long` {
      parse[Long]("1") must beEqualTo(1L)
    }

    def `should be readable as a JValue` {
      parse[JValue]("1") must beEqualTo(JInt(1))
    }

    // TODO: 11/29/10 <coda> -- add BigInt support
  }

  class `Parsing a JSON float` {
    def `should be readable as a Float` {
      parse[Float]("1.1") must beEqualTo(1.1F)
    }

    def `should be readable as a Double` {
      parse[Double]("1.1") must beEqualTo(1.1)
    }

    def `should be readable as a JValue` {
      parse[JValue]("1.1") must beEqualTo(JFloat(1.1))
    }

    // TODO: 11/29/10 <coda> -- add BigDecimal support
  }

  class `Parsing a JSON string` {
    def `should be readable as a String` {
      parse[String]("\"woo\"") must beEqualTo("woo")
    }

    def `should be readable as a JValue` {
      parse[JValue]("\"woo\"") must beEqualTo(JString("woo"))
    }
  }

  class `Parsing a JSON null` {
    def `should be readable as a null Object` {
      parse[String]("null") must beNull
    }

    def `should be readable as an Option[_]` {
      parse[Option[String]]("null") must beNone
    }

    def `should be readable as a JValue` {
      parse[JValue]("null") must beEqualTo(JNull)
    }

    // REVIEW: 11/29/10 <coda> -- should this also produce empty seqs?
  }

  class `Parsing a JSON array of ints` {
    def `should be readable as a Seq[Int]` {
      parse[Seq[Int]]("[1,2,3,4]") must beEqualTo(Seq(1, 2, 3, 4))
    }

    def `should be readable as a IndexedSeq[Int]` {
      parse[IndexedSeq[Int]]("[1,2,3,4]") must beEqualTo(IndexedSeq(1, 2, 3, 4))
    }

    def `should be readable as a List[Int]` {
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
    }

    def `should be readable as a Vector[Int]` {
      parse[Vector[Int]]("[1,2,3,4]") must beEqualTo(Vector(1, 2, 3, 4))
    }

    def `should be readable as a JValue` {
      parse[JValue]("[1,2,3,4]") must beEqualTo(JArray(List(JInt(1),
                                                            JInt(2),
                                                            JInt(3),
                                                            JInt(4))))
    }
  }

  class `Parsing a JSON array of ints with nulls` {
    def `should be readable as a List[Option[Int]]` {
      parse[List[Option[Int]]]("[1,2,null,4]") must beEqualTo(List(Some(1),
                                                                   Some(2),
                                                                   None,
                                                                   Some(4)))
    }
  }

  class `Parsing a JSON object with int field values` {
    def `should be readable as a Map[String, Int]` {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
    }

    def `should be readable as a JValue` {
      parse[JValue](""" {"one":1, "two": 2} """) must beEqualTo(JObject(List(JField("one", JInt(1)),
                                                                             JField("two", JInt(2)))))
    }
  }

  class `Parsing a JSON object as a case class` {
    def `should use all available constructors` {
      parse[Person](""" {"id":1, "name": "Coda"} """) must beEqualTo(Person(1, "Coda"))

      parse[Person](""" {"id":1, "firstName": "Coda", "lastName": "Hale"} """) must beEqualTo(Person(1, "Coda Hale"))
    }

    def `should handle missing Option members` {
      parse[ClassWithOption](""" {"one": "1"} """) must beEqualTo(ClassWithOption("1", None))

      parse[ClassWithOption](""" {"one": "1", "two": "2"} """) must beEqualTo(ClassWithOption("1", Some("2")))
    }

    def `should handle JsonNode parameters` {
      parse[ClassWithJsonNode](""" {"one": "1", "two": 2} """) must beEqualTo(ClassWithJsonNode("1", new IntNode(2)))
    }
  }

  class `Parsing a JSON value as a JsonNode` {
    def `should return a JsonNode` {
      parse[JsonNode]("[1, null, 2.0]").toString must beEqualTo("[1,null,2.0]")
    }
  }
}

case class Person(id: Long, name: String) {
  def this(id: Long, firstName: String, lastName: String) = this(id, firstName + " " + lastName)
}

case class ClassWithOption(one: String, two: Option[String])

case class ClassWithJsonNode(one: String, two: JsonNode)
