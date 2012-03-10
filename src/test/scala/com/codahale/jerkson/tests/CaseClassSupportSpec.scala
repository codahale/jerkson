package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import org.codehaus.jackson.node.IntNode
import org.junit.Test

class CaseClassSupportSpec extends Spec {
  class `A basic case class` {
    @Test def `generates a JSON object with matching field values` = {
      generate(CaseClass(1, "Coda")).must(be("""{"id":1,"name":"Coda"}"""))
    }

    @Test def `is parsable from a JSON object with corresponding fields` = {
      parse[CaseClass]("""{"id":1,"name":"Coda"}""").must(be(CaseClass(1, "Coda")))
    }

    @Test def `is parsable from a JSON object with extra fields` = {
      parse[CaseClass]("""{"id":1,"name":"Coda","derp":100}""").must(be(CaseClass(1, "Coda")))
    }

    @Test def `is not parsable from an incomplete JSON object` = {
      evaluating {
        parse[CaseClass]("""{"id":1}""")
      }.must(throwA[ParsingException]("""Invalid JSON. Needed [id, name], but found [id]."""))
    }
  }

  class `A case class with lazy fields` {
    @Test def `generates a JSON object with those fields evaluated` = {
      generate(CaseClassWithLazyVal(1)).must(be("""{"id":1,"woo":"yeah"}"""))
    }

    @Test def `is parsable from a JSON object without those fields` = {
      parse[CaseClassWithLazyVal]("""{"id":1}""").must(be(CaseClassWithLazyVal(1)))
    }

    @Test def `is not parsable from an incomplete JSON object` = {
      evaluating {
        parse[CaseClassWithLazyVal]("""{}""")
      }.must(throwA[ParsingException]("""Invalid JSON. Needed [id], but found []."""))
    }
  }

  class `A case class with ignored members` {
    @Test def `generates a JSON object without those fields` = {
      generate(CaseClassWithIgnoredField(1)).must(be("""{"id":1}"""))
      generate(CaseClassWithIgnoredFields(1)).must(be("""{"id":1}"""))
    }

    @Test def `is parsable from a JSON object without those fields` = {
      parse[CaseClassWithIgnoredField]("""{"id":1}""").must(be(CaseClassWithIgnoredField(1)))
      parse[CaseClassWithIgnoredFields]("""{"id":1}""").must(be(CaseClassWithIgnoredFields(1)))
    }

    @Test def `is not parsable from an incomplete JSON object` = {
      evaluating {
        parse[CaseClassWithIgnoredField]("""{}""")
      }.must(throwA[ParsingException]("""Invalid JSON. Needed [id], but found []."""))

      evaluating {
        parse[CaseClassWithIgnoredFields]("""{}""")
      }.must(throwA[ParsingException]("""Invalid JSON. Needed [id], but found []."""))
    }
  }

  class `A case class with transient members` {
    @Test def `generates a JSON object without those fields` = {
      generate(CaseClassWithTransientField(1)).must(be("""{"id":1}"""))
    }

    @Test def `is parsable from a JSON object without those fields` = {
      parse[CaseClassWithTransientField]("""{"id":1}""").must(be(CaseClassWithTransientField(1)))
    }

    @Test def `is not parsable from an incomplete JSON object` = {
      evaluating {
        parse[CaseClassWithTransientField]("""{}""")
      }.must(throwA[ParsingException]("""Invalid JSON. Needed [id], but found []."""))
    }
  }

  class `A case class with an overloaded field` {
    @Test def `generates a JSON object with the nullary version of that field` = {
      generate(CaseClassWithOverloadedField(1)).must(be("""{"id":1}"""))
    }
  }

  class `A case class with an Option[String] member` {
    @Test def `generates a field if the member is Some` = {
      generate(CaseClassWithOption(Some("what"))).must(be("""{"value":"what"}"""))
    }

    @Test def `is parsable from a JSON object with that field` = {
      parse[CaseClassWithOption]("""{"value":"what"}""").must(be(CaseClassWithOption(Some("what"))))
    }

    @Test def `doesn't generate a field if the member is None` = {
      generate(CaseClassWithOption(None)).must(be("""{}"""))
    }

    @Test def `is parsable from a JSON object without that field` = {
      parse[CaseClassWithOption]("""{}""").must(be(CaseClassWithOption(None)))
    }

    @Test def `is parsable from a JSON object with a null value for that field` = {
      parse[CaseClassWithOption]("""{"value":null}""").must(be(CaseClassWithOption(None)))
    }
  }

  class `A case class with a JsonNode member` {
    @Test def `generates a field of the given type` = {
      generate(CaseClassWithJsonNode(new IntNode(2))).must(be("""{"value":2}"""))
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


    @Test def `is parsable from a JSON object with those fields` = {
      parse[CaseClassWithAllTypes](json).must(be(
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
      ))
    }
  }

  class `A case class nested inside of an object` {
    @Test def `is parsable from a JSON object` = {
      parse[OuterObject.NestedCaseClass]("""{"id": 1}""").must(be(OuterObject.NestedCaseClass(1)))
    }
  }

  class `A case class nested inside of an object nested inside of an object` {
    @Test def `is parsable from a JSON object` = {
      parse[OuterObject.InnerObject.SuperNestedCaseClass]("""{"id": 1}""").must(be(OuterObject.InnerObject.SuperNestedCaseClass(1)))
    }
  }

  class `A case class with two constructors` {
    @Test def `is parsable from a JSON object with the same parameters as the case accessor` = {
      parse[CaseClassWithTwoConstructors]("""{"id":1,"name":"Bert"}""").must(be(CaseClassWithTwoConstructors(1, "Bert")))
    }

    @Test def `is parsable from a JSON object which works with the second constructor` = {
      evaluating {
        parse[CaseClassWithTwoConstructors]("""{"id":1}""")
      }.must(throwA[ParsingException])
    }
  }

  class `A case class with snake-cased fields` {
    @Test def `is parsable from a snake-cased JSON object` = {
      parse[CaseClassWithSnakeCase]("""{"one_thing":"yes","two_thing":"good"}""").must(be(CaseClassWithSnakeCase("yes", "good")))
    }

    @Test def `generates a snake-cased JSON object` = {
      generate(CaseClassWithSnakeCase("yes", "good")).must(be("""{"one_thing":"yes","two_thing":"good"}"""))
    }

    @Test def `throws errors with the snake-cased field names present` = {
      evaluating {
        parse[CaseClassWithSnakeCase]("""{"one_thing":"yes"}""")
      }.must(throwA[ParsingException]("Invalid JSON. Needed [one_thing, two_thing], but found [one_thing]."))
    }
  }

  class `A case class with array members` {
    @Test def `is parsable from a JSON object` = {
      val c = parse[CaseClassWithArrays]("""{"one":"1","two":["a","b","c"],"three":[1,2,3]}""")

      c.one.must(be("1"))
      c.two.must(be(Array("a", "b", "c")))
      c.three.must(be(Array(1, 2, 3)))
    }

    @Test def `generates a JSON object` = {
      generate(CaseClassWithArrays("1", Array("a", "b", "c"), Array(1, 2, 3))).must(be(
        """{"one":"1","two":["a","b","c"],"three":[1,2,3]}"""
      ))
    }
  }

  class `A case class with JsonTypeInfo annotation` {
    @Test def `serializes the type info to json` = {
      generate(CaseClassWithTypeInfo1(1, "Coda")) must be("""{"@c":".CaseClassWithTypeInfo1","id":1,"name":"Coda"}""")
      generate(CaseClassWithTypeInfo2(2, "Hale")) must be("""{"@c":".CaseClassWithTypeInfo2","id":2,"name":"Hale"}""")
    }
    @Test def `is parsable from a JSON object` = {
      parse[WithTypeInfo]("""{"@c":".CaseClassWithTypeInfo1","id":1,"name":"Coda"}""") must be(CaseClassWithTypeInfo1(1, "Coda"))
      parse[WithTypeInfo]("""{"@c":".CaseClassWithTypeInfo2","id":2,"name":"Hale"}""") must be(CaseClassWithTypeInfo2(2, "Hale"))
    }
  }

  class `A case class without values` {
    @Test def `is parsable from an empty JSON object` = {
      parse[CaseClassWithoutValues]("""{}""") must be(CaseClassWithoutValues())
    }

    @Test def `generates an empty JSON object` = {
      generate(CaseClassWithoutValues()) must be("""{}""")
    }
  }

}
