package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.AST._
import com.codahale.simplespec.Spec
import org.junit.Test

class ASTTypeSupportSpec extends Spec {
  class `An AST.JInt` {
    @Test def `generates a JSON int` = {
      generate(JInt(15)).must(be("15"))
    }

    @Test def `is parsable from a JSON int` = {
      parse[JInt]("15").must(be(JInt(15)))
    }

    @Test def `is parsable from a JSON int as a JValue` = {
      parse[JValue]("15").must(be(JInt(15)))
    }
  }

  class `An AST.JFloat` {
    @Test def `generates a JSON int` = {
      generate(JFloat(15.1)).must(be("15.1"))
    }

    @Test def `is parsable from a JSON float` = {
      parse[JFloat]("15.1").must(be(JFloat(15.1)))
    }

    @Test def `is parsable from a JSON float as a JValue` = {
      parse[JValue]("15.1").must(be(JFloat(15.1)))
    }
  }


  class `An AST.JString` {
    @Test def `generates a JSON string` = {
      generate(JString("woo")).must(be("\"woo\""))
    }

    @Test def `is parsable from a JSON string` = {
      parse[JString]("\"woo\"").must(be(JString("woo")))
    }

    @Test def `is parsable from a JSON string as a JValue` = {
      parse[JValue]("\"woo\"").must(be(JString("woo")))
    }
  }

  class `An AST.JNull` {
    @Test def `generates a JSON null` = {
      generate(JNull).must(be("null"))
    }

    @Test def `is parsable from a JSON null` = {
      parse[JNull.type]("null").must(be(JNull))
    }

    @Test def `is parsable from a JSON null as a JValue` = {
      parse[JValue]("null").must(be(JNull))
    }

    @Test def `is parsable from an embedded JSON null as a JValue` = {
      parse[JValue]("[null]").must(be(JArray(List(JNull))))
    }
  }

  class `An AST.JBoolean` {
    @Test def `generates a JSON true` = {
      generate(JBoolean(true)).must(be("true"))
    }

    @Test def `generates a JSON false` = {
      generate(JBoolean(false)).must(be("false"))
    }

    @Test def `is parsable from a JSON true` = {
      parse[JBoolean]("true").must(be(JBoolean(true)))
    }

    @Test def `is parsable from a JSON false` = {
      parse[JBoolean]("false").must(be(JBoolean(false)))
    }

    @Test def `is parsable from a JSON true as a JValue` = {
      parse[JValue]("true").must(be(JBoolean(true)))
    }

    @Test def `is parsable from a JSON false as a JValue` = {
      parse[JValue]("false").must(be(JBoolean(false)))
    }
  }

  class `An AST.JArray of JInts` {
    @Test def `generates a JSON array of ints` = {
      generate(JArray(List(JInt(1), JInt(2), JInt(3)))).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[JArray]("[1,2,3]").must(be(JArray(List(JInt(1), JInt(2), JInt(3)))))
    }

    @Test def `is parsable from a JSON array of ints as a JValue` = {
      parse[JValue]("[1,2,3]").must(be(JArray(List(JInt(1), JInt(2), JInt(3)))))
    }
  }

  class `An AST.JObject` {
    val obj = JObject(List(JField("id", JInt(1)), JField("name", JString("Coda"))))

    @Test def `generates a JSON object with matching field values` = {
      generate(obj).must(be("""{"id":1,"name":"Coda"}"""))
    }

    @Test def `is parsable from a JSON object` = {
      parse[JObject]("""{"id":1,"name":"Coda"}""").must(be(obj))
    }

    @Test def `is parsable from a JSON object as a JValue` = {
      parse[JValue]("""{"id":1,"name":"Coda"}""").must(be(obj))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[JObject]("""{}""").must(be(JObject(Nil)))
    }
  }
}
