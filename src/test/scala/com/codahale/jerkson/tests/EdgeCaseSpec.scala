package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import java.io.ByteArrayInputStream
import com.codahale.simplespec.annotation.test

class EdgeCaseSpec extends Spec {
  class `Deserializing lists` {
    @test def `doesn't cache Seq builders` = {
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
      parse[List[Int]]("[1,2,3,4]") must beEqualTo(List(1, 2, 3, 4))
    }
  }

  class `Parsing a JSON array of ints with nulls` {
    @test def `should be readable as a List[Option[Int]]` = {
      parse[List[Option[Int]]]("[1,2,null,4]") must beEqualTo(List(Some(1),
                                                                   Some(2),
                                                                   None,
                                                                   Some(4)))
    }
  }

  class `Deserializing maps` {
    @test def `doesn't cache Map builders` = {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
      parse[Map[String, Int]](""" {"one":1, "two": 2} """) must beEqualTo(Map("one" -> 1,
                                                                              "two" -> 2))
    }
  }

  class `Parsing malformed JSON` {
    @test def `should throw a ParsingException with an informative message` = {
      parse[Boolean]("jjf8;09") must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo(
            "Malformed JSON. Unexpected character ('j' (code 106)): expected a " +
                    "valid value (number, String, array, object, 'true', 'false' " +
                    "or 'null') at character offset 0."
          )
        }
      }

      parse[CaseClass]("{\"ye\":1") must throwA[ParsingException].like {
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
    @test def `should throw a ParsingException with an informative message` = {
      parse[CaseClass]("900") must throwA[ParsingException](
        """Can not deserialize instance of com.codahale.jerkson.tests.CaseClass out of VALUE_NUMBER_INT token\n""" +
          """ at \[Source: java.io.StringReader@[0-9a-f]+; line: 1, column: 1\]"""
      )

      parse[CaseClass]("{\"woo\": 1}") must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo("Invalid JSON. Needed [id, name], " +
                                              "but found [woo].")
        }
      }
    }
  }

  class `Parsing an empty document` {
    @test def `should throw a ParsingException with an informative message` = {
      val input = new ByteArrayInputStream(Array.empty)
      parse[CaseClass](input) must throwA[ParsingException].like {
        case e: ParsingException => {
          e.getMessage must beEqualTo("JSON document ended unexpectedly.")
        }
      }
    }
  }
}
