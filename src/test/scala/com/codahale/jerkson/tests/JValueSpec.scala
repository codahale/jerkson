package com.codahale.jerkson.tests

import com.codahale._
import simplespec._
import jerkson._
import Json._
import AST._

object JValueSpec extends Spec {
  class `selecting single nodes` {
    def `should return None with primitives` {
      parse[JValue]("8") \ "blah" must beNone
    }
    
    def `should return None on nonexistant fields` {
      parse[JValue]("{\"butt\": \"poop\"}") \ "anus" must beNone
    }
    
    def `should return a JValue with an existing field` {
      parse[JValue]("{\"butt\": \"poop\"}") \ "butt" must beSome(JString("poop"))
    }
  }
  
  class `selecting array members` {
    def `should return  None with primitives` {
      parse[JValue]("\"derp\"").apply(0) must beNone
    }
    
    def `should return None on out of bounds` {
      parse[JValue]("[0, 1, 2, 3]").apply(4) must beNone
    }
    
    def `should return a JValue` {
      parse[JValue]("[0, 1, 2, 3]").apply(2) must beSome(JInt(2))
    }
  }
  
  class `deep select` {
    def `should return None with primitives` {
      parse[JValue]("0.234") \\ "herp" must be(Nil)
    }
    
    def `should return None on nothing found` {
      parse[JValue]("{\"butt\": {\"anus\" : \"poopoo\"}}") \\ "anus" must beEqualTo(Seq(JString("poopoo")))
    }
    
    def `should return multiple leaf nodes` {
      parse[JValue]("{\"butt\": {\"anus\" : \"poopoo\"}, \"rectum\": {\"anus\" : \"dookie\"}}") \\ "anus" must beEqualTo(Seq(JString("poopoo"),JString("dookie")))
    }
  }
}