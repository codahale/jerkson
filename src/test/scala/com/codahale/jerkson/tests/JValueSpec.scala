package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import com.codahale.simplespec.Spec

class JValueSpec extends Spec {
  class `Selecting single nodes` {
    def `should return None with primitives` = {
      parse[JValue]("8") \ "blah" must be(JNull)
    }
    
    def `should return None on nonexistent fields` = {
      parse[JValue]("{\"butt\": \"poop\"}") \ "anus" must be(JNull)
    }
    
    def `should return a JValue with an existing field` = {
      parse[JValue]("{\"butt\": \"poop\"}") \ "butt" must beEqualTo(JString("poop"))
    }
  }
  
  class `Selecting array members` {
    def `should return  None with primitives` = {
      parse[JValue]("\"derp\"").apply(0) must be(JNull)
    }
    
    def `should return None on out of bounds` = {
      parse[JValue]("[0, 1, 2, 3]").apply(4) must be(JNull)
    }
    
    def `should return a JValue` = {
      parse[JValue]("[0, 1, 2, 3]").apply(2) must beEqualTo(JInt(2))
    }
  }
  
  class `Deep selecting` {
    def `should return None with primitives` = {
      parse[JValue]("0.234") \\ "herp" must be(Nil)
    }
    
    def `should return None on nothing found` = {
      parse[JValue]("{\"butt\": {\"anus\" : \"poopoo\"}}") \\ "anus" must beEqualTo(Seq(JString("poopoo")))
    }
    
    def `should return multiple leaf nodes` = {
      parse[JValue]("{\"butt\": {\"anus\" : \"poopoo\"}, \"rectum\": {\"anus\" : \"dookie\"}}") \\ "anus" must beEqualTo(Seq(JString("poopoo"),JString("dookie")))
    }
  }
}
