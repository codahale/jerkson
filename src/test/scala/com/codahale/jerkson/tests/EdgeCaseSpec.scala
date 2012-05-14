package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import java.io.ByteArrayInputStream
import org.junit.Test

class EdgeCaseSpec extends Spec {
  class `Deserializing lists` {
    @Test def `doesn't cache Seq builders` = {
      parse[List[Int]]("[1,2,3,4]").must(be(List(1, 2, 3, 4)))
      parse[List[Int]]("[1,2,3,4]").must(be(List(1, 2, 3, 4)))
    }
  }

  class `Parsing a JSON array of ints with nulls` {
    @Test def `should be readable as a List[Option[Int]]` = {
      parse[List[Option[Int]]]("[1,2,null,4]").must(be(List(Some(1), Some(2), None, Some(4))))
    }
  }

  class `Deserializing maps` {
    @Test def `doesn't cache Map builders` = {
      parse[Map[String, Int]](""" {"one":1, "two": 2} """).must(be(Map("one" -> 1, "two" -> 2)))
      parse[Map[String, Int]](""" {"one":1, "two": 2} """).must(be(Map("one" -> 1, "two" -> 2)))
    }
  }

  class `Parsing malformed JSON` {
    @Test def `should throw a ParsingException with an informative message` = {
      evaluating {
        parse[Boolean]("jjf8;09")
      }.must(throwA[ParsingException](
            "Malformed JSON. Unexpected character ('j' (code 106)): expected a " +
                    "valid value (number, String, array, object, 'true', 'false' " +
                    "or 'null') at character offset 0."))

      evaluating {
        parse[CaseClass]("{\"ye\":1")
      }.must(throwA[ParsingException](
            "Malformed JSON. Unexpected end-of-input: expected close marker for " +
                    "OBJECT at character offset 20."))
    }
  }

  class `Parsing invalid JSON` {
    @Test def `should throw a ParsingException with an informative message` = {
      evaluating {
        parse[CaseClass]("900")
      }.must(throwA[ParsingException](
        ("""Can not deserialize instance of com.codahale.jerkson.tests.CaseClass out of VALUE_NUMBER_INT token\n""" +
          """ at \[Source: java.io.StringReader@[0-9a-f]+; line: 1, column: 1\]""").r))

      evaluating {
        parse[CaseClass]("{\"woo\": 1}")
      }.must(throwA[ParsingException]("Invalid JSON. Needed [id, name], but found [woo]."))
    }
  }

  class `Parsing an empty document` {
    @Test def `should throw a ParsingException with an informative message` = {
      val input = new ByteArrayInputStream(Array.empty)
      evaluating {
        parse[CaseClass](input)
      }.must(throwA[ParsingException]("""No content to map due to end\-of\-input""".r))
    }
  }
}
