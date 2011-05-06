package com.codahale.jerkson.tests

import org.specs2.mutable.Specification
import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._

class JValueSpec extends Specification {
  "Selecting single nodes" should {
    "return None with primitives" in {
      parse[JValue]("8") \ "blah" must be(JNull)
    }
    
    "return None on nonexistant fields" in {
      parse[JValue]("{\"butt\": \"poop\"}") \ "anus" must be(JNull)
    }
    
    "return a JValue with an existing field" in {
      parse[JValue]("{\"butt\": \"poop\"}") \ "butt" must beEqualTo(JString("poop"))
    }
  }
  
  "Selecting array members" should {
    "return  None with primitives" in {
      parse[JValue]("\"derp\"").apply(0) must be(JNull)
    }
    
    "return None on out of bounds" in {
      parse[JValue]("[0, 1, 2, 3]").apply(4) must be(JNull)
    }
    
    "return a JValue" in {
      parse[JValue]("[0, 1, 2, 3]").apply(2) must beEqualTo(JInt(2))
    }
  }
  
  "Deep selecting" should {
    "return None with primitives" in {
      parse[JValue]("0.234") \\ "herp" must be(Nil)
    }
    
    "return None on nothing found" in {
      parse[JValue]("{\"butt\": {\"anus\" : \"poopoo\"}}") \\ "anus" must beEqualTo(Seq(JString("poopoo")))
    }
    
    "return multiple leaf nodes" in {
      parse[JValue]("{\"butt\": {\"anus\" : \"poopoo\"}, \"rectum\": {\"anus\" : \"dookie\"}}") \\ "anus" must beEqualTo(Seq(JString("poopoo"),JString("dookie")))
    }
  }
}
