package com.codahale.jerkson.tests

import org.junit.Assert.assertEquals
import org.junit.Test
import com.codahale.jerkson.Json
import com.codahale.jerkson.ParsingException

class TupleSerializationTest {
  @Test
  def serializedAsArray(): Unit = {
    val l = (1, "two", 3.0)
    assertEquals("[1,\"two\",3.0]", Json.generate(l))
  }
  
  @Test
  def parsedAsArrayWithAllIntegers(): Unit = {
    val expected = (1, 2, 3)
    assertEquals(expected, Json.parse[(Int, Int, Int)]("[1, 2, 3]"))
  }
  
  @Test
  def parsedAsArray(): Unit = {
    val expected = (1, "two", 3.0)
    assertEquals(expected, Json.parse[(Int, String, Double)]("[1, \"two\", 3.0]"))
  }
  
  @Test(expected = classOf[ParsingException])
  def errorWhenTooFewFieldsForTuple(): Unit = {
    Json.parse[(Int, Int)]("[1]")
  }
  
  @Test(expected = classOf[ParsingException])
  def errorWhenTooManyFieldsForTuple(): Unit = {
    Json.parse[(Int)]("[1,2]")
  }
  
  @Test
  def longestTupleLengthIsParsedWithAllIntegers(): Unit = {
    val numbers  = 1 to 22
    val str = "[" + numbers.mkString(",") + "]"

    val t = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    
    assertEquals(t,
        Json.parse[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)](str))
  }
}
