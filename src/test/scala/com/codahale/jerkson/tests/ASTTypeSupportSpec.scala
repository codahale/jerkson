package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test

class ASTTypeSupportSpec extends Spec {
  class `An AST.JInt` {
    @test def `generates a JSON int` = {
      generate(JInt(15)).mustEqual("15")
    }

    @test def `is parsable from a JSON int` = {
      parse[JInt]("15").mustEqual(JInt(15))
    }

    @test def `is parsable from a JSON int as a JValue` = {
      parse[JValue]("15").mustEqual(JInt(15))
    }
  }

  class `An AST.JFloat` {
    @test def `generates a JSON int` = {
      generate(JFloat(15.1)).mustEqual("15.1")
    }

    @test def `is parsable from a JSON float` = {
      parse[JFloat]("15.1").mustEqual(JFloat(15.1))
    }

    @test def `is parsable from a JSON float as a JValue` = {
      parse[JValue]("15.1").mustEqual(JFloat(15.1))
    }
  }


  class `An AST.JString` {
    @test def `generates a JSON string` = {
      generate(JString("woo")).mustEqual("\"woo\"")
    }

    @test def `is parsable from a JSON string` = {
      parse[JString]("\"woo\"").mustEqual(JString("woo"))
    }

    @test def `is parsable from a JSON string as a JValue` = {
      parse[JValue]("\"woo\"").mustEqual(JString("woo"))
    }
  }

  class `An AST.JNull` {
    @test def `generates a JSON null` = {
      generate(JNull).mustEqual("null")
    }

    @test def `is parsable from a JSON null` = {
      parse[JNull.type]("null").mustEqual(JNull)
    }

    @test def `is parsable from a JSON null as a JValue` = {
      parse[JValue]("null").mustEqual(JNull)
    }
  }

  class `An AST.JBoolean` {
    @test def `generates a JSON true` = {
      generate(JBoolean(true)).mustEqual("true")
    }

    @test def `generates a JSON false` = {
      generate(JBoolean(false)).mustEqual("false")
    }

    @test def `is parsable from a JSON true` = {
      parse[JBoolean]("true").mustEqual(JBoolean(true))
    }

    @test def `is parsable from a JSON false` = {
      parse[JBoolean]("false").mustEqual(JBoolean(false))
    }

    @test def `is parsable from a JSON true as a JValue` = {
      parse[JValue]("true").mustEqual(JBoolean(true))
    }

    @test def `is parsable from a JSON false as a JValue` = {
      parse[JValue]("false").mustEqual(JBoolean(false))
    }
  }

  class `An AST.JArray of JInts` {
    @test def `generates a JSON array of ints` = {
      generate(JArray(List(JInt(1), JInt(2), JInt(3)))).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[JArray]("[1,2,3]").mustEqual(JArray(List(JInt(1), JInt(2), JInt(3))))
    }

    @test def `is parsable from a JSON array of ints as a JValue` = {
      parse[JValue]("[1,2,3]").mustEqual(JArray(List(JInt(1), JInt(2), JInt(3))))
    }
  }

  class `An AST.JObject` {
    val obj = JObject(List(JField("id", JInt(1)), JField("name", JString("Coda"))))

    @test def `generates a JSON object with matching field values` = {
      generate(obj).mustEqual("""{"id":1,"name":"Coda"}""")
    }

    @test def `is parsable from a JSON object` = {
      parse[JObject]("""{"id":1,"name":"Coda"}""").mustEqual(obj)
    }

    @test def `is parsable from a JSON object as a JValue` = {
      parse[JValue]("""{"id":1,"name":"Coda"}""").mustEqual(obj)
    }

    @test def `is parsable from an empty JSON object` = {
      parse[JObject]("""{}""").mustEqual(JObject(Nil))
    }
  }
}
