package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class ASTTypeSupportSpec extends FreeSpec with MustMatchers {
  "An AST.JInt" - {
     "generates a JSON int" in {
      generate(JInt(15)).must(be("15"))
    }

     "is parsable from a JSON int" in {
      parse[JInt]("15").must(be(JInt(15)))
    }

     "is parsable from a JSON int as a JValue" in {
      parse[JValue]("15").must(be(JInt(15)))
    }
  }

  "An AST.JFloat" - {
     "generates a JSON int" in {
      generate(JFloat(15.1)).must(be("15.1"))
    }

     "is parsable from a JSON float" in {
      parse[JFloat]("15.1").must(be(JFloat(15.1)))
    }

     "is parsable from a JSON float as a JValue" in {
      parse[JValue]("15.1").must(be(JFloat(15.1)))
    }
  }


  "An AST.JString" - {
     "generates a JSON string" in {
      generate(JString("woo")).must(be("\"woo\""))
    }

     "is parsable from a JSON string" in {
      parse[JString]("\"woo\"").must(be(JString("woo")))
    }

     "is parsable from a JSON string as a JValue" in {
      parse[JValue]("\"woo\"").must(be(JString("woo")))
    }
  }

  "An AST.JNull" - {
     "generates a JSON null" in {
      generate(JNull).must(be("null"))
    }

     "is parsable from a JSON null" in {
      parse[JNull.type]("null").must(be(JNull))
    }

     "is parsable from a JSON null as a JValue" in {
      parse[JValue]("null").must(be(JNull))
    }
  }

  "An AST.JBoolean" - {
     "generates a JSON true" in {
      generate(JBoolean(true)).must(be("true"))
    }

     "generates a JSON false" in {
      generate(JBoolean(false)).must(be("false"))
    }

     "is parsable from a JSON true" in {
      parse[JBoolean]("true").must(be(JBoolean(true)))
    }

     "is parsable from a JSON false" in {
      parse[JBoolean]("false").must(be(JBoolean(false)))
    }

     "is parsable from a JSON true as a JValue" in {
      parse[JValue]("true").must(be(JBoolean(true)))
    }

     "is parsable from a JSON false as a JValue" in {
      parse[JValue]("false").must(be(JBoolean(false)))
    }
  }

  "An AST.JArray of JInts" - {
     "generates a JSON array of ints" in {
      generate(JArray(List(JInt(1), JInt(2), JInt(3)))).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[JArray]("[1,2,3]").must(be(JArray(List(JInt(1), JInt(2), JInt(3)))))
    }

     "is parsable from a JSON array of ints as a JValue" in {
      parse[JValue]("[1,2,3]").must(be(JArray(List(JInt(1), JInt(2), JInt(3)))))
    }
  }

  "An AST.JObject" - {
    val obj = JObject(List(JField("id", JInt(1)), JField("name", JString("Coda"))))

     "generates a JSON object with matching field values" in {
      generate(obj).must(be("""{"id":1,"name":"Coda"}"""))
    }

     "is parsable from a JSON object" in {
      parse[JObject]("""{"id":1,"name":"Coda"}""").must(be(obj))
    }

     "is parsable from a JSON object as a JValue" in {
      parse[JValue]("""{"id":1,"name":"Coda"}""").must(be(obj))
    }

     "is parsable from an empty JSON object" in {
      parse[JObject]("""{}""").must(be(JObject(Nil)))
    }
  }
}
