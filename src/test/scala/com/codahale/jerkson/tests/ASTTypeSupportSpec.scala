package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import com.codahale.simplespec.Spec

class ASTTypeSupportSpec extends Spec {
  class `An AST.JInt` {
    def `generates a JSON int` = {
      generate(JInt(15)) must beEqualTo("15")
    }

    def `is parsable from a JSON int` = {
      parse[JInt]("15") must beEqualTo(JInt(15))
    }

    def `is parsable from a JSON int as a JValue` = {
      parse[JValue]("15") must beEqualTo(JInt(15))
    }
  }

  class `An AST.JFloat` {
    def `generates a JSON int` = {
      generate(JFloat(15.1)) must beEqualTo("15.1")
    }

    def `is parsable from a JSON float` = {
      parse[JFloat]("15.1") must beEqualTo(JFloat(15.1))
    }

    def `is parsable from a JSON float as a JValue` = {
      parse[JValue]("15.1") must beEqualTo(JFloat(15.1))
    }
  }


  class `An AST.JString` {
    def `generates a JSON string` = {
      generate(JString("woo")) must beEqualTo("\"woo\"")
    }

    def `is parsable from a JSON string` = {
      parse[JString]("\"woo\"") must beEqualTo(JString("woo"))
    }

    def `is parsable from a JSON string as a JValue` = {
      parse[JValue]("\"woo\"") must beEqualTo(JString("woo"))
    }
  }

  class `An AST.JNull` {
    def `generates a JSON null` = {
      generate(JNull) must beEqualTo("null")
    }

    def `is parsable from a JSON null` = {
      parse[JNull.type]("null") must beEqualTo(JNull)
    }

    def `is parsable from a JSON null as a JValue` = {
      parse[JValue]("null") must beEqualTo(JNull)
    }
  }

  class `An AST.JBoolean` {
    def `generates a JSON true` = {
      generate(JBoolean(true)) must beEqualTo("true")
    }

    def `generates a JSON false` = {
      generate(JBoolean(false)) must beEqualTo("false")
    }

    def `is parsable from a JSON true` = {
      parse[JBoolean]("true") must beEqualTo(JBoolean(true))
    }

    def `is parsable from a JSON false` = {
      parse[JBoolean]("false") must beEqualTo(JBoolean(false))
    }

    def `is parsable from a JSON true as a JValue` = {
      parse[JValue]("true") must beEqualTo(JBoolean(true))
    }

    def `is parsable from a JSON false as a JValue` = {
      parse[JValue]("false") must beEqualTo(JBoolean(false))
    }
  }

  class `An AST.JArray of JInts` {
    def `generates a JSON array of ints` = {
      generate(JArray(List(JInt(1), JInt(2), JInt(3)))) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[JArray]("[1,2,3]") must beEqualTo(JArray(List(JInt(1), JInt(2), JInt(3))))
    }

    def `is parsable from a JSON array of ints as a JValue` = {
      parse[JValue]("[1,2,3]") must beEqualTo(JArray(List(JInt(1), JInt(2), JInt(3))))
    }
  }

  class `An AST.JObject` {
    private val obj = JObject(List(JField("id", JInt(1)), JField("name", JString("Coda"))))

    def `generates a JSON object with matching field values` = {
      generate(obj) must beEqualTo("""{"id":1,"name":"Coda"}""")
    }

    def `is parsable from a JSON object` = {
      parse[JObject]("""{"id":1,"name":"Coda"}""") must beEqualTo(obj)
    }

    def `is parsable from a JSON object as a JValue` = {
      parse[JValue]("""{"id":1,"name":"Coda"}""") must beEqualTo(obj)
    }

    def `is parsable from an empty JSON object` = {
      parse[JObject]("""{}""") must beEqualTo(JObject(Nil))
    }
  }
}
