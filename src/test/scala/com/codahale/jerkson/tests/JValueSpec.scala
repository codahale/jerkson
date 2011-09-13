package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import com.codahale.simplespec.Spec
import org.junit.Test

class JValueSpec extends Spec {
  class `Selecting single nodes` {
    @Test def `returns None with primitives` = {
      (parse[JValue]("8") \ "blah").must(be(JNull))
    }
    
    @Test def `returns None on nonexistent fields` = {
      (parse[JValue]("{\"one\": \"1\"}") \ "two").must(be(JNull))
    }
    
    @Test def `returns a JValue with an existing field` = {
      (parse[JValue]("{\"one\": \"1\"}") \ "one").must(be(JString("1")))
    }
  }
  
  class `Selecting array members` {
    @Test def `returns None with primitives` = {
      (parse[JValue]("\"derp\"").apply(0)).must(be(JNull))
    }
    
    @Test def `returns None on out of bounds` = {
      (parse[JValue]("[0, 1, 2, 3]").apply(4)).must(be(JNull))
    }
    
    @Test def `returns a JValue` = {
      (parse[JValue]("[0, 1, 2, 3]").apply(2)).must(be(JInt(2)))
    }
  }
  
  class `Deep selecting` {
    @Test def `returns Nil with primitives` = {
      (parse[JValue]("0.234") \\ "herp").must(be(empty))
    }

    @Test def `returns Nil on nothing found` = {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}}") \\ "four").must(be(empty))
    }
    
    @Test def `returns single leaf nodes` = {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}}") \\ "two").must(be(Seq(JString("three"))))
    }
    
    @Test def `should return multiple leaf nodes` = {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}, \"four\": {\"two\" : \"five\"}}") \\ "two").must(be(Seq(JString("three"),JString("five"))))
    }
  }
}
