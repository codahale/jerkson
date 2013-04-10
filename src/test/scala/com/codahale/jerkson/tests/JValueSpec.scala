package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class JValueSpec extends FreeSpec with MustMatchers {
  "Selecting single nodes" - {
     "returns None with primitives" in {
      (parse[JValue]("8") \ "blah").must(be(JNull))
    }
    
     "returns None on nonexistent fields" in {
      (parse[JValue]("{\"one\": \"1\"}") \ "two").must(be(JNull))
    }
    
     "returns a JValue with an existing field" in {
      (parse[JValue]("{\"one\": \"1\"}") \ "one").must(be(JString("1")))
    }
  }
  
  "Selecting array members" - {
     "returns None with primitives" in {
      (parse[JValue]("\"derp\"").apply(0)).must(be(JNull))
    }
    
     "returns None on out of bounds" in {
      (parse[JValue]("[0, 1, 2, 3]").apply(4)).must(be(JNull))
    }
    
     "returns a JValue" in {
      (parse[JValue]("[0, 1, 2, 3]").apply(2)).must(be(JInt(2)))
    }
  }
  
  "Deep selecting" - {
     "returns Nil with primitives" in {
      (parse[JValue]("0.234") \\ "herp").must(be('empty))
    }

     "returns Nil on nothing found" in {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}}") \\ "four").must(be('empty))
    }
    
     "returns single leaf nodes" in {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}}") \\ "two").must(be(Seq(JString("three"))))
    }
    
     "should return multiple leaf nodes" in {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}, \"four\": {\"two\" : \"five\"}}") \\ "two").must(be(Seq(JString("three"),JString("five"))))
    }
  }
}
