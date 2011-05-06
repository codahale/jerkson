package com.codahale.jerkson.tests

import com.codahale.jerkson.AST._
import com.codahale.jerkson.Json._
import org.codehaus.jackson.annotate.JsonIgnore
import org.specs2.mutable.Specification

class JsonGenerationSpec extends Specification {
  "An Int" should {
    "generate a JSON int" in {
      generate(15) must beEqualTo("15")
    }
  }

  "A JInt" should {
    "generate a JSON int" in {
      generate(JInt(15)) must beEqualTo("15")
    }
  }

  "A Long" should {
    "generate a JSON int" in {
      generate(15L) must beEqualTo("15")
    }
  }

  "A BigInt" should {
    "generate a JSON int" in {
      generate(BigInt(15)) must beEqualTo("15")
    }
  }

  "A Float" should {
    "generate a JSON float" in {
      generate(15.1F) must beEqualTo("15.1")
    }
  }

  "A JFloat" should {
    "generate a JSON int" in {
      generate(JFloat(15.1)) must beEqualTo("15.1")
    }
  }

  "A Double" should {
    "generate a JSON float" in {
      generate(15.1) must beEqualTo("15.1")
    }
  }

  "A BigDecimal" should {
    "generate a JSON float" in {
      generate(BigDecimal(15.5)) must beEqualTo("15.5")
    }
  }

  "A String" should {
    "generate a JSON string" in {
      generate("woo") must beEqualTo("\"woo\"")
    }
  }

  "A JString" should {
    "generate a JSON string" in {
      generate(JString("woo")) must beEqualTo("\"woo\"")
    }
  }

  "A null Object" should {
    "generate a JSON null" in {
      generate[Object](null) must beEqualTo("null")
    }
  }

  "A JNull" should {
    "generate a JSON null" in {
      generate(JNull) must beEqualTo("null")
    }
  }

  "A Boolean" should {
    "generate a JSON true" in {
      generate(true) must beEqualTo("true")
    }

    "generate a JSON false" in {
      generate(false) must beEqualTo("false")
    }
  }

  "A JBoolean" should {
    "generate a JSON true" in {
      generate(JBoolean(true)) must beEqualTo("true")
    }

    "generate a JSON false" in {
      generate(JBoolean(false)) must beEqualTo("false")
    }
  }

  "A Some[Int]" should {
    "generate a JSON int" in {
      generate(Some(12)) must beEqualTo("12")
    }
  }

  "A None" should {
    "generate a JSON null" in {
      generate(None) must beEqualTo("null")
    }
  }

  "A Seq[Int]" should {
    "generate a JSON array of ints" in {
      generate(Seq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  "A List[Int]" should {
    "generate a JSON array of ints" in {
      generate(List(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  "A IndexedSeq[Int]" should {
    "generate a JSON array of ints" in {
      generate(IndexedSeq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  "A Vector[Int]" should {
    "generate a JSON array of ints" in {
      generate(Vector(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  "A Set[Int]" should {
    "generate a JSON array of ints" in {
      generate(Set(1)) must beEqualTo("[1]")
    }
  }

  "An Iterator[Int]" should {
    "generate a JSON array of ints" in {
      generate(Seq(1, 2, 3).iterator) must beEqualTo("[1,2,3]")
    }
  }

  "A JArray of JInts" should {
    "generate a JSON array of ints" in {
      generate(JArray(List(JInt(1), JInt(2), JInt(3)))) must beEqualTo("[1,2,3]")
    }
  }

  "A Map[String, Int]" should {
    "generate a JSON object with int field values" in {
      generate(Map("one" -> 1, "two" -> 2)) must beEqualTo("""{"one":1,"two":2}""")
    }
  }

  "A Map[String, Any]" should {
    "generate a JSON object with mixed field values" in {
      generate(Map("one" -> 1, "two" -> "2")) must beEqualTo("""{"one":1,"two":"2"}""")
    }
  }

  "A case class" should {
    "generate a JSON object with matching field values" in {
      generate(Person(1, "Coda")) must beEqualTo("""{"id":1,"name":"Coda"}""")
    }
  }

  "A case class with lazy fields" should {
    "generate a JSON object with those fields evaluated" in {
      generate(CaseClassWithLazyVal(1)) must beEqualTo("""{"id":1,"woo":"yeah"}""")
    }
  }

  "A case class with ignored fields" should {
    "generate a JSON object without those fields" in {
      generate(CaseClassWithIgnoredField(1)) must beEqualTo("""{"id":1}""")
    }
  }

  "A case class with an overloaded field" should {
    "use the single-arity version" in {
      generate(CaseClassWithOverloadedField(1)) must beEqualTo("""{"id":1}""")
    }
  }

  "A case class with a Some(x) field" should {
    "generate a field" in {
      generate(CaseClassWithOption(Some("what"))) must beEqualTo("""{"value":"what"}""")
    }
  }

  "A case class with a None field" should {
    "generate a field" in {
      generate(CaseClassWithOption(None)) must beEqualTo("""{}""")
    }
  }

  "A JObject" should {
    "generate a JSON object with matching field values" in {
      generate(JObject(List(JField("id", JInt(1)),
                            JField("name", JString("Coda"))))) must beEqualTo("""{"id":1,"name":"Coda"}""")
    }
  }

  "A Left[String]" should {
    "generate a JSON string" in {
      generate(Left("woo")) must beEqualTo("\"woo\"")
    }
  }

  "A Right[String]" should {
    "generate a JSON string" in {
      generate(Right("woo")) must beEqualTo("\"woo\"")
    }
  }
}

case class CaseClassWithLazyVal(id: Long) {
  lazy val woo = "yeah"
}

case class CaseClassWithIgnoredField(id: Long) {
  @JsonIgnore
  val uncomfortable = "Bad Touch"
}

case class CaseClassWithOverloadedField(id: Long) {
  def id(prefix: String): String = prefix + id
}

case class CaseClassWithOption(value: Option[String])
