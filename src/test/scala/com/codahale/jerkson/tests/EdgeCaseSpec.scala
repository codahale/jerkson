package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.ParsingException
import java.io.ByteArrayInputStream
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

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
      evaluating {
        parse[Boolean]("jjf8;09")
      }.must(produce[ParsingException])

      evaluating {
        parse[CaseClass]("{\"ye\":1")
      }.must(produce[ParsingException])
    }
  }

  "Parsing invalid JSON" - {
     "should throw a ParsingException with an informative message" in {
      evaluating {
        parse[CaseClass]("900")
      }.must(produce[ParsingException])

      evaluating {
        parse[CaseClass]("{\"woo\": 1}")
      }.must(produce[ParsingException])
    }
  }

  "Parsing an empty document" - {
     "should throw a ParsingException with an informative message" in {
      val input = new ByteArrayInputStream(Array.empty)
      evaluating {
        parse[CaseClass](input)
      }.must(produce[ParsingException])
    }
  }
}
