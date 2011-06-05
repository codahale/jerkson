package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.Json._
import org.codehaus.jackson.node.IntNode
import org.codehaus.jackson.JsonNode

class BasicTypeSupportSpec extends Spec {
  class `A Byte` {
    def `generates a JSON int` = {
      generate(15.toByte) must beEqualTo("15")
    }

    def `is parsable from a JSON int` = {
      parse[Byte]("15") must beEqualTo(15)
    }
  }

  class `A Short` {
    def `generates a JSON int` = {
      generate(15.toShort) must beEqualTo("15")
    }

    def `is parsable from a JSON int` = {
      parse[Short]("15") must beEqualTo(15)
    }
  }

  class `An Int` {
    def `generates a JSON int` = {
      generate(15) must beEqualTo("15")
    }

    def `is parsable from a JSON int` = {
      parse[Int]("15") must beEqualTo(15)
    }
  }

  class `A Long` {
    def `generates a JSON int` = {
      generate(15L) must beEqualTo("15")
    }

    def `is parsable from a JSON int` = {
      parse[Long]("15") must beEqualTo(15L)
    }
  }

  class `A BigInt` {
    def `generates a JSON int` = {
      generate(BigInt(15)) must beEqualTo("15")
    }

    def `is parsable from a JSON int` = {
      parse[BigInt]("15") must beEqualTo(BigInt(15))
    }
  }

  class `A Float` {
    def `generates a JSON float` = {
      generate(15.1F) must beEqualTo("15.1")
    }

    def `is parsable from a JSON float` = {
      parse[Float]("15.1") must beEqualTo(15.1F)
    }
  }

  class `A Double` {
    def `generates a JSON float` = {
      generate(15.1) must beEqualTo("15.1")
    }

    def `is parsable from a JSON float` = {
      parse[Double]("15.1") must beEqualTo(15.1D)
    }
  }

  class `A BigDecimal` {
    def `generates a JSON float` = {
      generate(BigDecimal(15.5)) must beEqualTo("15.5")
    }

    def `is parsable from a JSON float` = {
      parse[BigDecimal]("15.5") must beEqualTo(BigDecimal(15.5))
    }
  }

  class `A String` {
    def `generates a JSON string` = {
      generate("woo") must beEqualTo("\"woo\"")
    }

    def `is parsable from a JSON string` = {
      parse[String]("\"woo\"") must beEqualTo("woo")
    }
  }

  class `A StringBuilder` {
    def `generates a JSON string` = {
      generate(new StringBuilder("foo")) must beEqualTo("\"foo\"")
    }

    def `is parsable from a JSON string` = {
      parse[StringBuilder]("\"foo\"").toString must beEqualTo("foo")
    }
  }

  class `A null Object` {
    def `generates a JSON null` = {
      generate[Object](null) must beEqualTo("null")
    }

    def `is parsable from a JSON null` = {
      parse[Object]("null") must beNull
    }
  }

  class `A Boolean` {
    def `generates a JSON true` = {
      generate(true) must beEqualTo("true")
    }

    def `generates a JSON false` = {
      generate(false) must beEqualTo("false")
    }

    def `is parsable from a JSON true` = {
      parse[Boolean]("true") must beTrue
    }

    def `is parsable from a JSON false` = {
      parse[Boolean]("false") must beFalse
    }
  }

  class `A Some[Int]` {
    def `generates a JSON int` = {
      generate(Some(12)) must beEqualTo("12")
    }

    def `is parsable from a JSON int as an Option[Int]` = {
      parse[Option[Int]]("12") must beSome(12)
    }
  }

  class `A None` {
    def `generates a JSON null` = {
      generate(None) must beEqualTo("null")
    }

    def `is parsable from a JSON null as an Option[Int]` = {
      parse[Option[Int]]("null") must beNone
    }
  }

  class `A Left[String]` {
    def `generates a JSON string` = {
      generate(Left("woo")) must beEqualTo("\"woo\"")
    }

    def `is parsable from a JSON string as an Either[String, Int]` = {
      parse[Either[String, Int]]("\"woo\"") must beLeft("woo")
    }
  }

  class `A Right[String]` {
    def `generates a JSON string` = {
      generate(Right("woo")) must beEqualTo("\"woo\"")
    }

    def `is parsable from a JSON string as an Either[Int, String]` = {
      parse[Either[Int, String]]("\"woo\"") must beRight("woo")
    }
  }

  class `A JsonNode` {
    def `generates whatever the JsonNode is` = {
      generate(new IntNode(2)) must beEqualTo("2")
    }

    def `is parsable from a JSON AST node` = {
      parse[JsonNode]("2") must beEqualTo(new IntNode(2))
    }

    def `is parsable from a JSON AST node as a specific type` = {
      parse[IntNode]("2") must beEqualTo(new IntNode(2))
    }

    def `is itself parsable` = {
      parse[Int](new IntNode(2)) must beEqualTo(2)
    }
  }

  class `An Array[Int]` {
    def `generates a JSON array of ints` = {
      generate(Array(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[Array[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Array[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }
}
