package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.node.IntNode
import com.codahale.jerkson.AST._
import collection.mutable.ArrayBuffer
import java.io.ByteArrayInputStream
import com.codahale.jerkson.ParsingException
import com.codahale.simplespec.Spec

object JsonParsingSpec extends Spec {
  class `Parsing malformed JSON` {
    def `should throw a ParsingException with an informative message` = {
      parse[Boolean]("jjf8;09") must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo(
            "Malformed JSON. Unexpected character ('j' (code 106)): expected a " +
                    "valid value (number, String, array, object, 'true', 'false' " +
                    "or 'null') at character offset 0."
          )
        }
      }

      parse[Person]("{\"ye\":1") must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo(
            "Malformed JSON. Unexpected end-of-input: expected close marker for " +
                    "OBJECT at character offset 20."
          )
        }
      }
    }
  }

  class `Parsing invalid JSON` {
    def `should throw a ParsingException with an informative message` = {
      parse[Person]("900") must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo("Invalid JSON.")
        }
      }

      parse[Person]("{\"woo\": 1}") must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo("Invalid JSON. Needed [id, name], " +
                                              "but found [woo].")
        }
      }
    }
  }

  class `Parsing an empty document` {
    def `should throw a ParsingException with an informative message` = {
      val input = new ByteArrayInputStream(Array.empty)
      parse[Person](input) must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo("JSON document ended unexpectedly.")
        }
      }
    }
  }

  class `Parsing a JSON boolean` {
    def `should be readable as a Boolean` = {
      parse[Boolean]("true") must beTrue
      parse[Boolean]("false") must beFalse
    }

    def `should be readable as a JValue` = {
      parse[JValue]("true") must beEqualTo(JBoolean(true))
      parse[JValue]("false") must beEqualTo(JBoolean(false))
    }
  }

  class `Parsing a JSON int` {
    def `should be readable as an Int` = {
      parse[Int]("1") must beEqualTo(1)
    }

    def `should be readable as an Option[Int]` = {
      parse[Option[Int]]("1") must beSome(1)
    }

    def `should be readable as a Long` = {
      parse[Long]("1") must beEqualTo(1L)
    }

    def `should be readable as a JValue` = {
      parse[JValue]("1") must beEqualTo(JInt(1))
    }

    def `should be readable as a BigInt` = {
      parse[BigInt]("1") must beEqualTo(BigInt(1))
    }
  }

  class `Parsing a JSON float` {
    def `should be readable as a Float` = {
      parse[Float]("1.1") must beEqualTo(1.1F)
    }

    def `should be readable as a Double` = {
      parse[Double]("1.1") must beEqualTo(1.1)
    }

    def `should be readable as a JValue` = {
      parse[JValue]("1.1") must beEqualTo(JFloat(1.1))
    }

    def `should be readable as a BigDecimal` = {
      parse[BigDecimal]("1.1") must beEqualTo(BigDecimal(1.1))
    }
  }

  class `Parsing a JSON string` {
    def `should be readable as a String` = {
      parse[String]("\"woo\"") must beEqualTo("woo")
    }

    def `should be readable as a JValue` = {
      parse[JValue]("\"woo\"") must beEqualTo(JString("woo"))
    }
  }

  class `Parsing a JSON null` {
    def `should be readable as a null Object` = {
      parse[String]("null") must beNull
    }

    def `should be readable as an Option[_]` = {
      parse[Option[String]]("null") must beNone
    }

    def `should be readable as a JValue` = {
      parse[JValue]("null") must beEqualTo(JNull)
    }

    // REVIEW: 11/29/10 <coda> -- should this also produce empty seqs?
  }

  class `Parsing a JSON array of ints` {
    def `should be readable as a Seq[Int]` = {
      parse[Seq[Int]]("[1,2,3,4]") must beEqualTo(Seq(1, 2, 3, 4))
    }

    def `should be readable as a IndexedSeq[Int]` = {
      parse[IndexedSeq[Int]]("[1,2,3,4]") must beEqualTo(IndexedSeq(1, 2, 3, 4))
    }

    def `should be readable as a List[Int]` = {
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
    }

    def `should be readable as a Vector[Int]` = {
      parse[Vector[Int]]("[1,2,3,4]") must beEqualTo(Vector(1, 2, 3, 4))
    }

    def `should be readable as a JValue` = {
      parse[JValue]("[1,2,3,4]") must beEqualTo(JArray(List(JInt(1),
                                                            JInt(2),
                                                            JInt(3),
                                                            JInt(4))))
    }

    def `should be readable as a Set[Int]` = {
      parse[Set[Int]]("[1,2,3,4]") must beEqualTo(Set(1, 2, 3, 4))
    }

    def `should be readable as an Iterator[Int]` = {
      parse[Iterator[Int]]("[1,2,3,4]").toSeq must beEqualTo(Seq(1, 2, 3, 4))
    }
  }

  class `Caching a JSON array deserializer` {
    def `should not cache Seq builders` = {
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
    }
  }

  class `Parsing a JSON array of ints with nulls` {
    def `should be readable as a List[Option[Int]]` = {
      parse[List[Option[Int]]]("[1,2,null,4]") must beEqualTo(List(Some(1),
                                                                   Some(2),
                                                                   None,
                                                                   Some(4)))
    }
  }

  class `Parsing an empty JSON object` {
    def `should be readable as a Map` = {
      parse[Map[String, Int]]("{}") must beEqualTo(Map.empty)
    }

    def `should be readable as a case class with a single Option[_] value` = {
      parse[CaseClassWithOption]("{}") must beEqualTo(CaseClassWithOption(None))
    }
  }

  class `Parsing a JSON object with int field values` {
    def `should be readable as a Map[String, Int]` = {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
    }

    def `should be readable as a JValue` = {
      parse[JValue](""" {"one":1, "two": 2} """) must beEqualTo(JObject(List(JField("one", JInt(1)),
                                                                             JField("two", JInt(2)))))
    }
  }

  class `Caching a JSON map deserializer` {
    def `should not cache Map builders` = {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
    }
  }
  
  class `Parsing a JSON object as a case class` {
    def `should handle missing Option members` = {
      parse[ClassWithOption](""" {"one": "1"} """) must beEqualTo(ClassWithOption("1", None))

      parse[ClassWithOption](""" {"one": "1", "two": "2"} """) must beEqualTo(ClassWithOption("1", Some("2")))
    }

    def `should handle JsonNode parameters` = {
      parse[ClassWithJsonNode](""" {"one": "1", "two": 2} """) must beEqualTo(ClassWithJsonNode("1", new IntNode(2)))
    }
  }

  class `Parsing a JSON value as a JsonNode` {
    def `should return a JsonNode` = {
      parse[JsonNode]("[1, null, 2.0]").toString must beEqualTo("[1,null,2.0]")
    }
  }

  class `Parsing a stream of objects` {
    private val json = """[
      {"id":1, "name": "Coda"},
      {"id":2, "name": "Niki"},
      {"id":3, "name": "Biscuit"},
      {"id":4, "name": "Louie"}
    ]"""

    def `should fire a callback for each stream element` = {
      val input = new ByteArrayInputStream(json.getBytes)

      val people = new ArrayBuffer[Person]
      parseStreamOf[Person](input) { p =>
        people += p
      }

      people.toSeq must beEqualTo(Seq(Person(1, "Coda"),
                                      Person(2, "Niki"),
                                      Person(3, "Biscuit"),
                                      Person(4, "Louie")))
    }

    def `should return an iterator of stream elements` = {
      val input = new ByteArrayInputStream(json.getBytes)

      val people = new ArrayBuffer[Person]
      stream[Person](input).toList must beEqualTo(List(Person(1, "Coda"),
                                                       Person(2, "Niki"),
                                                       Person(3, "Biscuit"),
                                                       Person(4, "Louie")))
    }
  }

  class `Parsing an int JSON node` {
    private val node = parse[JsonNode]("1")

    def `should return an Int` = {
      parse[Int](node) must beEqualTo(1)
    }
  }

  class `Parsing a JSON int as an Either[Int, String]` {
    def `should return a Left` = {
      parse[Either[Int, String]]("1") must beEqualTo(Left(1))
    }
  }

  class `Parsing a JSON string as an Either[Int, String]` {
    def `should return a Right` = {
      parse[Either[Int, String]]("\"woo\"") must beEqualTo(Right("woo"))
    }
  }

  class `Parsing a JSON object as a case class with Map arguments` {
    def `should use the Scala signature to detect map value types` = {
      parse[ClassWithMap]("""{"properties": {"yay": 400}}""") must beEqualTo(ClassWithMap(Map("yay" -> 400L)))
    }
  }

  class `Parsing a JSON object with an array of strings` {
    def `should parse as a List[String] member of a case class` = {
      parse[ClassWithList]("""{"roles":["badass","beardo"]}""") must beEqualTo(ClassWithList(List("badass", "beardo")))
    }

    def `should parse as a Seq[String] member of a case class` = {
      parse[ClassWithSeq]("""{"roles":["badass","beardo"]}""") must beEqualTo(ClassWithSeq(Seq("badass", "beardo")))
    }

    def `should parse as an IndexedSeq[String] member of a case class` = {
      parse[ClassWithIndexedSeq]("""{"roles":["badass","beardo"]}""") must beEqualTo(ClassWithIndexedSeq(IndexedSeq("badass", "beardo")))
    }
  }
}

case class Person(id: Long, name: String)

case class ClassWithOption(one: String, two: Option[String])

case class ClassWithJsonNode(one: String, two: JsonNode)

case class ClassWithMap(properties: Map[String, Long])

case class ClassWithList(roles: List[String])

case class ClassWithSeq(roles: Seq[String])

case class ClassWithIndexedSeq(roles: IndexedSeq[String])
