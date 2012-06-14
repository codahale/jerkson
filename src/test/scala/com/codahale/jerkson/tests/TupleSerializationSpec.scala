package com.codahale.jerkson.tests

import org.junit.Test
import com.codahale.jerkson.Json
import com.codahale.jerkson.ParsingException
import com.codahale.simplespec.Spec

class TupleSerializationTest extends Spec {
  @Test def serializedAsArray() = {
    Json.generate((1, "two", 3.0)).must(be("[1,\"two\",3.0]"))
  }

  @Test def parsedAsArrayWithAllIntegers() = {
    Json.parse[(Int, Int, Int)]("[1, 2, 3]").must(be((1, 2, 3)))
  }

  @Test def parsedAsArray() = {
    Json.parse[(Int, String, Double)]("[1, \"two\", 3.0]").must(be((1, "two", 3.0)))
  }

  @Test(expected = classOf[ParsingException])
  def errorWhenTooFewFieldsForTuple() = {
    Json.parse[(Int, Int)]("[1]")
  }

  @Test(expected = classOf[ParsingException])
  def errorWhenTooManyFieldsForTuple() = {
    Json.parse[(Int)]("[1,2]")
  }

  @Test def longestTupleLengthIsParsedWithAllIntegers() = {
    val numbers  = 1 to 22
    val str = "[" + numbers.mkString(",") + "]"

    val t = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)

    Json.parse[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)](str).must(
        be(t))
  }
}
