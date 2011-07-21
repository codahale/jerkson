package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import org.codehaus.jackson.node.IntNode
import com.codahale.simplespec.annotation.test

class CaseClassSupportSpec extends Spec {
  class `A basic case class` {
    @test def `generates a JSON object with matching field values` = {
      generate(CaseClass(1, "Coda")) must
        beEqualTo("""{"id":1,"name":"Coda"}""")
    }

    @test def `is parsable from a JSON object with corresponding fields` = {
      parse[CaseClass]("""{"id":1,"name":"Coda"}""") must
        beEqualTo(CaseClass(1, "Coda"))
    }

    @test def `is parsable from a JSON object with extra fields` = {
      parse[CaseClass]("""{"id":1,"name":"Coda","derp":100}""") must
        beEqualTo(CaseClass(1, "Coda"))
    }

    @test def `is not parsable from a JSON object which doesn't include all of the matching field values` = {
      parse[CaseClass]("""{"id":1}""") must
        throwA[ParsingException]("""Invalid JSON. Needed \[id, name\], but found \[id\].""")
    }
  }

  class `A case class with lazy fields` {
    @test def `generates a JSON object with those fields evaluated` = {
      generate(CaseClassWithLazyVal(1)) must
        beEqualTo("""{"id":1,"woo":"yeah"}""")
    }

    @test def `is parsable from a JSON object without those fields` = {
      parse[CaseClassWithLazyVal]("""{"id":1}""") must
        beEqualTo(CaseClassWithLazyVal(1))
    }

    @test def `is not parsable from a JSON object which doesn't include all of the matching field values` = {
      parse[CaseClassWithLazyVal]("""{}""") must
        throwA[ParsingException]("""Invalid JSON. Needed \[id], but found \[\].""")
    }
  }

  class `A case class with ignored members` {
    @test def `generates a JSON object without those fields` = {
      generate(CaseClassWithIgnoredField(1)) must beEqualTo("""{"id":1}""")
    }

    @test def `is parsable from a JSON object without those fields` = {
      parse[CaseClassWithIgnoredField]("""{"id":1}""") must
        beEqualTo(CaseClassWithIgnoredField(1))
    }

    @test def `is not parsable from a JSON object which doesn't include all of the matching fields` = {
      parse[CaseClassWithIgnoredField]("""{}""") must
        throwA[ParsingException]("""Invalid JSON. Needed \[id], but found \[\].""")
    }
  }

  class `A case class with an overloaded field` {
    @test def `generates a JSON object with the nullary version of that field` = {
      generate(CaseClassWithOverloadedField(1)) must beEqualTo("""{"id":1}""")
    }
  }

  class `A case class with an Option[String] member` {
    @test def `generates a field if the member is Some` = {
      generate(CaseClassWithOption(Some("what"))) must beEqualTo("""{"value":"what"}""")
    }

    @test def `is parsable from a JSON object with that field` = {
      parse[CaseClassWithOption]("""{"value":"what"}""") must
        beEqualTo(CaseClassWithOption(Some("what")))
    }

    @test def `doesn't generate a field if the member is None` = {
      generate(CaseClassWithOption(None)) must beEqualTo("""{}""")
    }

    @test def `is parsable from a JSON object without that field` = {
      parse[CaseClassWithOption]("""{}""") must
        beEqualTo(CaseClassWithOption(None))
    }

    @test def `is parsable from a JSON object with a null value for that field` = {
      parse[CaseClassWithOption]("""{"value":null}""") must
        beEqualTo(CaseClassWithOption(None))
    }
  }

  class `A case class with a JsonNode member` {
    @test def `generates a field of the given type` = {
      generate(CaseClassWithJsonNode(new IntNode(2))) must beEqualTo("""{"value":2}""")
    }
  }

  class `A case class with members of all ScalaSig types` {
    val json = """
               {
                 "map": {
                   "one": "two"
                 },
                 "set": [1, 2, 3],
                 "string": "woo",
                 "list": [4, 5, 6],
                 "seq": [7, 8, 9],
                 "sequence": [10, 11, 12],
                 "collection": [13, 14, 15],
                 "indexedSeq": [16, 17, 18],
                 "randomAccessSeq": [19, 20, 21],
                 "vector": [22, 23, 24],
                 "bigDecimal": 12.0,
                 "bigInt": 13,
                 "int": 1,
                 "long": 2,
                 "char": "x",
                 "bool": false,
                 "short": 14,
                 "byte": 15,
                 "float": 34.5,
                 "double": 44.9,
                 "any": true,
                 "anyRef": "wah",
                 "intMap": {
                   "1": "1"
                 },
                 "longMap": {
                   "2": 2
                 }
               }
               """


    @test def `is parsable from a JSON object with those fields` = {
      parse[CaseClassWithAllTypes](json) must beEqualTo(
        CaseClassWithAllTypes(
          map = Map("one" -> "two"),
          set = Set(1, 2, 3),
          string = "woo",
          list = List(4, 5, 6),
          seq = Seq(7, 8, 9),
          indexedSeq = IndexedSeq(16, 17, 18),
          vector = Vector(22, 23, 24),
          bigDecimal = BigDecimal("12.0"),
          bigInt = BigInt("13"),
          int = 1,
          long = 2L,
          char = 'x',
          bool = false,
          short = 14,
          byte = 15,
          float = 34.5f,
          double = 44.9d,
          any = true,
          anyRef = "wah",
          intMap = Map(1 -> 1),
          longMap = Map(2L -> 2L)
        )
      )
    }

  }
}
