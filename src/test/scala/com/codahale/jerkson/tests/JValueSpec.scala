package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test

class JValueSpec extends Spec {
  class `Selecting single nodes` {
    @test def `returns None with primitives` = {
      (parse[JValue]("8") \ "blah").mustEqual(JNull)
    }
    
    @test def `returns None on nonexistent fields` = {
      (parse[JValue]("{\"one\": \"1\"}") \ "two").mustEqual(JNull)
    }
    
    @test def `returns a JValue with an existing field` = {
      (parse[JValue]("{\"one\": \"1\"}") \ "one").mustEqual(JString("1"))
    }
  }
  
  class `Selecting array members` {
    @test def `returns None with primitives` = {
      (parse[JValue]("\"derp\"").apply(0)).mustEqual(JNull)
    }
    
    @test def `returns None on out of bounds` = {
      (parse[JValue]("[0, 1, 2, 3]").apply(4)).mustEqual(JNull)
    }
    
    @test def `returns a JValue` = {
      (parse[JValue]("[0, 1, 2, 3]").apply(2)).mustEqual(JInt(2))
    }
  }
  
  class `Deep selecting` {
    @test def `returns Nil with primitives` = {
      (parse[JValue]("0.234") \\ "herp").mustBeEmpty()
    }

    @test def `returns Nil on nothing found` = {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}}") \\ "four").mustBeEmpty()
    }
    
    @test def `returns single leaf nodes` = {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}}") \\ "two").mustEqual(Seq(JString("three")))
    }
    
    @test def `should return multiple leaf nodes` = {
      (parse[JValue]("{\"one\": {\"two\" : \"three\"}, \"four\": {\"two\" : \"five\"}}") \\ "two").mustEqual(Seq(JString("three"),JString("five")))
    }
  }
}
