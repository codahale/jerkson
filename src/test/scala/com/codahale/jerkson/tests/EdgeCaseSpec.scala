package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import org.scalatest.matchers.MustMatchers
import org.scalatest.FreeSpec

import com.codahale.jerkson.ParsingException
import java.io.ByteArrayInputStream

class EdgeCaseSpec extends FreeSpec with MustMatchers {
  "Deserializing lists" - {
    "doesn't cache Seq builders" in {
      parse[List[Int]]("[1,2,3,4]").must(be(List(1, 2, 3, 4)))
      parse[List[Int]]("[1,2,3,4]").must(be(List(1, 2, 3, 4)))
    }
  }

  "Parsing a JSON array of ints with nulls" - {
    "should be readable as a List[Option[Int]]" in {
      parse[List[Option[Int]]]("[1,2,null,4]").must(be(List(Some(1), Some(2), None, Some(4))))
    }
  }

  "Deserializing maps" - {
    "doesn't cache Map builders" in {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """).must(be(Map("one" -> 1, "two" -> 2)))
      parse[Map[String, Int]](""" {"one":1, "two": 2} """).must(be(Map("one" -> 1, "two" -> 2)))
    }
  }

  "Parsing malformed JSON" - {
    "should throw a ParsingException with an informative message" in {
      intercept[ParsingException] {
        parse[Boolean]("jjf8;09")
      }.getMessage must be(
            "Malformed JSON. Unexpected character ('j' (code 106)): expected a " +
                    "valid value (number, String, array, object, 'true', 'false' " +
                    "or 'null') at character offset 0.")

      intercept[ParsingException] {
        parse[CaseClass]("{\"ye\":1")
      }.getMessage must be(
            "Malformed JSON. Unexpected end-of-input: expected close marker for " +
                    "OBJECT at character offset 20.")
    }
  }

  "Parsing invalid JSON" - {
    "should throw a ParsingException with an informative message" in {
      intercept[ParsingException] {
        parse[CaseClass]("900")
      }.getMessage must fullyMatch regex(
        ("""Can not deserialize instance of com.codahale.jerkson.tests.CaseClass out of VALUE_NUMBER_INT token\n""" +
          """ at \[Source: java.io.StringReader@[0-9a-f]+; line: 1, column: 1\]""").r)

      intercept[ParsingException] {
        parse[CaseClass]("{\"woo\": 1}")
      }.getMessage must be("Invalid JSON. Needed [id, name], but found [woo].")
    }
  }

  "Parsing an empty document" - {
    "should throw a ParsingException with an informative message" in {
      val input = new ByteArrayInputStream(Array.empty)
      intercept[ParsingException] {
        parse[CaseClass](input)
      }.getMessage must startWith("""No content to map due to end-of-input""")
    }
  }
}
