package com.codahale.jerkson.tests

import com.codahale.jerkson.AST._
import com.codahale.jerkson.Json._
import org.codehaus.jackson.annotate.JsonIgnore
import com.codahale.simplespec.Spec
import scala.collection.{mutable, immutable}

object JsonGenerationSpec extends Spec {
  class `An Int` {
    def `should generate a JSON int` = {
      generate(15) must beEqualTo("15")
    }
  }

  class `A JInt` {
    def `should generate a JSON int` = {
      generate(JInt(15)) must beEqualTo("15")
    }
  }

  class `A Long` {
    def `should generate a JSON int` = {
      generate(15L) must beEqualTo("15")
    }
  }

  class `A BigInt` {
    def `should generate a JSON int` = {
      generate(BigInt(15)) must beEqualTo("15")
    }
  }

  class `A Float` {
    def `should generate a JSON float` = {
      generate(15.1F) must beEqualTo("15.1")
    }
  }

  class `A JFloat` {
    def `should generate a JSON int` = {
      generate(JFloat(15.1)) must beEqualTo("15.1")
    }
  }

  class `A Double` {
    def `should generate a JSON float` = {
      generate(15.1) must beEqualTo("15.1")
    }
  }

  class `A BigDecimal` {
    def `should generate a JSON float` = {
      generate(BigDecimal(15.5)) must beEqualTo("15.5")
    }
  }

  class `A String` {
    def `should generate a JSON string` = {
      generate("woo") must beEqualTo("\"woo\"")
    }
  }

  class `A JString` {
    def `should generate a JSON string` = {
      generate(JString("woo")) must beEqualTo("\"woo\"")
    }
  }

  class `A null Object` {
    def `should generate a JSON null` = {
      generate[Object](null) must beEqualTo("null")
    }
  }

  class `A JNull` {
    def `should generate a JSON null` = {
      generate(JNull) must beEqualTo("null")
    }
  }

  class `A Boolean` {
    def `should generate a JSON true` = {
      generate(true) must beEqualTo("true")
    }

    def `should generate a JSON false` = {
      generate(false) must beEqualTo("false")
    }
  }

  class `A JBoolean` {
    def `should generate a JSON true` = {
      generate(JBoolean(true)) must beEqualTo("true")
    }

    def `should generate a JSON false` = {
      generate(JBoolean(false)) must beEqualTo("false")
    }
  }

  class `A Some[Int]` {
    def `should generate a JSON int` = {
      generate(Some(12)) must beEqualTo("12")
    }
  }

  class `A None` {
    def `should generate a JSON null` = {
      generate(None) must beEqualTo("null")
    }
  }

  class `A Vector[Int]` {
    def `should generate a JSON array of ints` = {
      generate(Vector(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A Seq[Int]` {
    def `should generate a JSON array of ints` = {
      generate(Seq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A List[Int]` {
    def `should generate a JSON array of ints` = {
      generate(List(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A IndexedSeq[Int]` {
    def `should generate a JSON array of ints` = {
      generate(IndexedSeq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A Set[Int]` {
    def `should generate a JSON array of ints` = {
      generate(Set(1)) must beEqualTo("[1]")
    }
  }

  class `An Iterator[Int]` {
    def `should generate a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator) must beEqualTo("[1,2,3]")
    }
  }

  class `A JArray of JInts` {
    def `should generate a JSON array of ints` = {
      generate(JArray(List(JInt(1), JInt(2), JInt(3)))) must beEqualTo("[1,2,3]")
    }
  }

  class `A Map[String, Int]` {
    def `should generate a JSON object with int field values` = {
      generate(Map("one" -> 1, "two" -> 2)) must beEqualTo("""{"one":1,"two":2}""")
    }
  }

  class `A Map[String, Any]` {
    def `should generate a JSON object with mixed field values` = {
      generate(Map("one" -> 1, "two" -> "2")) must beEqualTo("""{"one":1,"two":"2"}""")
    }
  }

  class `A case class` {
    def `should generate a JSON object with matching field values` = {
      generate(Person(1, "Coda")) must beEqualTo("""{"id":1,"name":"Coda"}""")
    }
  }

  class `A case class with lazy fields` {
    def `should generate a JSON object with those fields evaluated` = {
      generate(CaseClassWithLazyVal(1)) must beEqualTo("""{"id":1,"woo":"yeah"}""")
    }
  }

  class `A case class with ignored fields` {
    def `should generate a JSON object without those fields` = {
      generate(CaseClassWithIgnoredField(1)) must beEqualTo("""{"id":1}""")
    }
  }

  class `A case class with an overloaded field` {
    def `should use the single-arity version` = {
      generate(CaseClassWithOverloadedField(1)) must beEqualTo("""{"id":1}""")
    }
  }

  class `A case class with a Some(x) field` {
    def `should generate a field` = {
      generate(CaseClassWithOption(Some("what"))) must beEqualTo("""{"value":"what"}""")
    }
  }

  class `A case class with a None field` {
    def `should generate a field` = {
      generate(CaseClassWithOption(None)) must beEqualTo("""{}""")
    }
  }

  class `A JObject` {
    def `should generate a JSON object with matching field values` = {
      generate(JObject(List(JField("id", JInt(1)),
                            JField("name", JString("Coda"))))) must beEqualTo("""{"id":1,"name":"Coda"}""")
    }
  }

  class `A Left[String]` {
    def `should generate a JSON string` = {
      generate(Left("woo")) must beEqualTo("\"woo\"")
    }
  }

  class `A Right[String]` {
    def `should generate a JSON string` = {
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

object JsonGenerationCollectionCoverageSpec extends Spec {

  // http://www.scala-lang.org/docu/files/collections-api/collections_2.html

  class `A Vector` {
    def `should generate a JSON array` = {
      generate(Vector(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A ResizableArray` {
    def `should generate a JSON array` = {
      generate(mutable.ResizableArray(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `An ArraySeq` {
    def `should generate a JSON array` = {
      generate(mutable.ArraySeq(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A StringBuilder` {
    def `should generate a JSON string` = {
      generate(new StringBuilder("foo")) must beEqualTo(generate("foo"))
    }
  }

  class `An Array` {
    def `should generate a JSON array` = {
      generate(Array(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A List` {
    def `should generate a JSON array` = {
      generate(List(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A Stream` {
    def `should generate a JSON array` = {
      generate(Stream(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A MutableList` {
    def `should generate a JSON array` = {
      generate({ val xs = new mutable.MutableList[Int]; xs ++= List(1, 2, 3); xs}) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A Queue` {
    def `should generate a JSON array` = {
      generate(mutable.Queue(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A ListBuffer` {
    def `should generate a JSON array` = {
      generate(mutable.ListBuffer(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `An ArrayBuffer` {
    def `should generate a JSON array` = {
      generate(mutable.ArrayBuffer(1, 2, 3)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A Range` {
    def `should generate a JSON array` = {
      generate(Range(1, 4)) must beEqualTo(generate(Seq(1, 2, 3)))
    }
  }

  class `A TreeSet` {
    def `should generate a JSON array` = {
      generate(immutable.TreeSet(1)) must beEqualTo("[1]")
    }
  }

  class `An immutable HashSet` {
    def `should generate a JSON array` = {
      generate(immutable.HashSet(1)) must beEqualTo("[1]")
    }
  }

  class `A mutable HashSet` {
    def `should generate a JSON array` = {
      generate(mutable.HashSet(1)) must beEqualTo("[1]")
    }
  }

  class `A LinkedHashSet` {
    def `should generate a JSON array` = {
      generate(mutable.LinkedHashSet(1)) must beEqualTo("[1]")
    }
  }

  class `A BitSet` {
    def `should generate a JSON array` = {
      generate(immutable.BitSet(1)) must beEqualTo("[1]")
    }
  }

  class `A Set` {
    def `should generate a JSON array` = {
      generate(Set(1)) must beEqualTo("[1]")
    }
  }

  class `A TreeMap` {
    def `should generate a JSON object` = {
      generate(immutable.TreeMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }
  }

  class `An immutable HashMap` {
    def `should generate a JSON object` = {
      generate(immutable.HashMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }
  }

  class `A mutable HashMap` {
    def `should generate a JSON object` = {
      generate(mutable.HashMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }
  }

  class `A LinkedHashMap` {
    def `should generate a JSON object` = {
      generate(mutable.LinkedHashMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }
  }

  class `A Map` {
    def `should generate a JSON object` = {
      generate(Map("one" -> 1)) must beEqualTo("""{"one":1}""")
    }
  }
}
