package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.node.IntNode
import com.codahale.jerkson.AST._
import collection.mutable.ArrayBuffer
import java.io.ByteArrayInputStream
import com.codahale.jerkson.ParsingException
import org.specs2.mutable.Specification

class JsonParsingSpec extends Specification {
  "Parsing malformed JSON" should {
    "throw a ParsingException with an informative message" in {
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

  "Parsing invalid JSON" should {
    "throw a ParsingException with an informative message" in {
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

  "Parsing an empty document" should {
    "throw a ParsingException with an informative message" in {
      val input = new ByteArrayInputStream(Array.empty)
      parse[Person](input) must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo("JSON document ended unexpectedly.")
        }
      }
    }
  }

  "Parsing a JSON boolean" should {
    "be readable as a Boolean" in {
      parse[Boolean]("true") must beTrue
      parse[Boolean]("false") must beFalse
    }

    "be readable as a JValue" in {
      parse[JValue]("true") must beEqualTo(JBoolean(true))
      parse[JValue]("false") must beEqualTo(JBoolean(false))
    }
  }

  "Parsing a JSON int" should {
    "be readable as an Int" in {
      parse[Int]("1") must beEqualTo(1)
    }

    "be readable as an Option[Int]" in {
      parse[Option[Int]]("1") must beSome(1)
    }

    "be readable as a Long" in {
      parse[Long]("1") must beEqualTo(1L)
    }

    "be readable as a JValue" in {
      parse[JValue]("1") must beEqualTo(JInt(1))
    }

    "be readable as a BigInt" in {
      parse[BigInt]("1") must beEqualTo(BigInt(1))
    }
  }

  "Parsing a JSON float" should {
    "be readable as a Float" in {
      parse[Float]("1.1") must beEqualTo(1.1F)
    }

    "be readable as a Double" in {
      parse[Double]("1.1") must beEqualTo(1.1)
    }

    "be readable as a JValue" in {
      parse[JValue]("1.1") must beEqualTo(JFloat(1.1))
    }

    "be readable as a BigDecimal" in {
      parse[BigDecimal]("1.1") must beEqualTo(BigDecimal(1.1))
    }
  }

  "Parsing a JSON string" should {
    "be readable as a String" in {
      parse[String]("\"woo\"") must beEqualTo("woo")
    }

    "be readable as a JValue" in {
      parse[JValue]("\"woo\"") must beEqualTo(JString("woo"))
    }
  }

  "Parsing a JSON null" should {
    "be readable as a null Object" in {
      parse[String]("null") must beNull
    }

    "be readable as an Option[_]" in {
      parse[Option[String]]("null") must beNone
    }

    "be readable as a JValue" in {
      parse[JValue]("null") must beEqualTo(JNull)
    }

    // REVIEW: 11/29/10 <coda> -- should this also produce empty seqs?
  }

  "Parsing a JSON array of ints" should {
    "be readable as a Seq[Int]" in {
      parse[Seq[Int]]("[1,2,3,4]") must beEqualTo(Seq(1, 2, 3, 4))
    }

    "be readable as a IndexedSeq[Int]" in {
      parse[IndexedSeq[Int]]("[1,2,3,4]") must beEqualTo(IndexedSeq(1, 2, 3, 4))
    }

    "be readable as a List[Int]" in {
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
    }

    "be readable as a Vector[Int]" in {
      parse[Vector[Int]]("[1,2,3,4]") must beEqualTo(Vector(1, 2, 3, 4))
    }

    "be readable as a JValue" in {
      parse[JValue]("[1,2,3,4]") must beEqualTo(JArray(List(JInt(1),
                                                            JInt(2),
                                                            JInt(3),
                                                            JInt(4))))
    }

    "be readable as a Set[Int]" in {
      parse[Set[Int]]("[1,2,3,4]") must beEqualTo(Set(1, 2, 3, 4))
    }

    "be readable as an Iterator[Int]" in {
      parse[Iterator[Int]]("[1,2,3,4]").toSeq must beEqualTo(Seq(1, 2, 3, 4))
    }
  }

  "Caching a JSON array deserializer" should {
    "not cache Seq builders" in {
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
    }
  }

  "Parsing a JSON array of ints with nulls" should {
    "be readable as a List[Option[Int]]" in {
      parse[List[Option[Int]]]("[1,2,null,4]") must beEqualTo(List(Some(1),
                                                                   Some(2),
                                                                   None,
                                                                   Some(4)))
    }
  }

  "Parsing an empty JSON object" should {
    "be readable as a Map" in {
      parse[Map[String, Int]]("{}") must beEqualTo(Map.empty)
    }

    "be readable as a case class with a single Option[_] value" in {
      parse[CaseClassWithOption]("{}") must beEqualTo(CaseClassWithOption(None))
    }
  }

  "Parsing a JSON object with int field values" should {
    "be readable as a Map[String, Int]" in {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
    }

    "be readable as a JValue" in {
      parse[JValue](""" {"one":1, "two": 2} """) must beEqualTo(JObject(List(JField("one", JInt(1)),
                                                                             JField("two", JInt(2)))))
    }
  }

  "Caching a JSON map deserializer" should {
    "not cache Map builders" in {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
    }
  }

  "Parsing a JSON object as a case class" should {
    "handle missing Option members" in {
      parse[ClassWithOption](""" {"one": "1"} """) must beEqualTo(ClassWithOption("1", None))

      parse[ClassWithOption](""" {"one": "1", "two": "2"} """) must beEqualTo(ClassWithOption("1", Some("2")))
    }

    "handle JsonNode parameters" in {
      parse[ClassWithJsonNode](""" {"one": "1", "two": 2} """) must beEqualTo(ClassWithJsonNode("1", new IntNode(2)))
    }
  }

  "Parsing a JSON value as a JsonNode" should {
    "return a JsonNode" in {
      parse[JsonNode]("[1, null, 2.0]").toString must beEqualTo("[1,null,2.0]")
    }
  }

  "Parsing a stream of objects" should {
    val json = """[
      {"id":1, "name": "Coda"},
      {"id":2, "name": "Niki"},
      {"id":3, "name": "Biscuit"},
      {"id":4, "name": "Louie"}
    ]"""

    "fire a callback for each stream element" in {
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

    "return an iterator of stream elements" in {
      val input = new ByteArrayInputStream(json.getBytes)

      val people = new ArrayBuffer[Person]
      stream[Person](input).toList must beEqualTo(List(Person(1, "Coda"),
                                                       Person(2, "Niki"),
                                                       Person(3, "Biscuit"),
                                                       Person(4, "Louie")))
    }
  }

  "Parsing a int JSON node" should {
    val node = parse[JsonNode]("1")

    "return an Int" in {
      parse[Int](node) must beEqualTo(1)
    }
  }

  "Parsing a JSON int as an Either[Int, String]" should {
    "return a Left" in {
      parse[Either[Int, String]]("1") must beEqualTo(Left(1))
    }
  }

  "Parsing a JSON string as an Either[Int, String]" should {
    "return a Right" in {
      parse[Either[Int, String]]("\"woo\"") must beEqualTo(Right("woo"))
    }
  }

  "Parsing a JSON object as a case class with Map arguments" should {
    "use the Scala signature to detect map value types" in {
      parse[ClassWithMap]("""{"properties": {"yay": 400}}""") must beEqualTo(ClassWithMap(Map("yay" -> 400L)))
    }
  }
}

case class Person(id: Long, name: String)

case class ClassWithOption(one: String, two: Option[String])

case class ClassWithJsonNode(one: String, two: JsonNode)

case class ClassWithMap(properties: Map[String, Long])
