package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.JsonNode
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class BasicTypeSupportSpec extends FreeSpec with MustMatchers {
  "A Byte" - {
     "generates a JSON int" in {
      generate(15.toByte).must(be("15"))
    }

     "is parsable from a JSON int" in {
      parse[Byte]("15").must(be(15))
    }
  }

  "A Short" - {
     "generates a JSON int" in {
      generate(15.toShort).must(be("15"))
    }

     "is parsable from a JSON int" in {
      parse[Short]("15").must(be(15))
    }
  }

  "An Int" - {
     "generates a JSON int" in {
      generate(15).must(be("15"))
    }

     "is parsable from a JSON int" in {
      parse[Int]("15").must(be(15))
    }
  }

  "A Long" - {
     "generates a JSON int" in {
      generate(15L).must(be("15"))
    }

     "is parsable from a JSON int" in {
      parse[Long]("15").must(be(15L))
    }
  }

  "A BigInt" - {
     "generates a JSON int" in {
      generate(BigInt(15)).must(be("15"))
    }

     "is parsable from a JSON int" in {
      parse[BigInt]("15").must(be(BigInt(15)))
    }

     "is parsable from a JSON string" in {
      parse[BigInt]("\"15\"").must(be(BigInt(15)))
    }
  }

  "A Float" - {
     "generates a JSON float" in {
      generate(15.1F).must(be("15.1"))
    }

     "is parsable from a JSON float" in {
      parse[Float]("15.1").must(be(15.1F))
    }
  }

  "A Double" - {
     "generates a JSON float" in {
      generate(15.1).must(be("15.1"))
    }

     "is parsable from a JSON float" in {
      parse[Double]("15.1").must(be(15.1D))
    }
  }

  "A BigDecimal" - {
     "generates a JSON float" in {
      generate(BigDecimal(15.5)).must(be("15.5"))
    }

     "is parsable from a JSON float" in {
      parse[BigDecimal]("15.5").must(be(BigDecimal(15.5)))
    }

     "is parsable from a JSON int" in {
      parse[BigDecimal]("15").must(be(BigDecimal(15.0)))
    }
  }

  "A String" - {
     "generates a JSON string" in {
      generate("woo").must(be("\"woo\""))
    }

     "is parsable from a JSON string" in {
      parse[String]("\"woo\"").must(be("woo"))
    }
  }

  "A StringBuilder" - {
     "generates a JSON string" in {
      generate(new StringBuilder("foo")).must(be("\"foo\""))
    }

     "is parsable from a JSON string" in {
      parse[StringBuilder]("\"foo\"").toString().must(be("foo"))
    }
  }

  "A null Object" - {
     "generates a JSON null" in {
      generate[Object](null).must(be("null"))
    }

     "is parsable from a JSON null" in {
      parse[Object]("null").must(be(null))
    }
  }

  "A Boolean" - {
     "generates a JSON true" in {
      generate(true).must(be("true"))
    }

     "generates a JSON false" in {
      generate(false).must(be("false"))
    }

     "is parsable from a JSON true" in {
      parse[Boolean]("true").must(be(true))
    }

     "is parsable from a JSON false" in {
      parse[Boolean]("false").must(be(false))
    }
  }

  "A Some[Int]" - {
     "generates a JSON int" in {
      generate(Some(12)).must(be("12"))
    }

     "is parsable from a JSON int as an Option[Int]" in {
      parse[Option[Int]]("12").must(be(Some(12)))
    }
  }

  "A None" - {
     "generates a JSON null" in {
      generate(None).must(be("null"))
    }

     "is parsable from a JSON null as an Option[Int]" in {
      parse[Option[Int]]("null").must(be(None))
    }
  }

  "A Left[String]" - {
     "generates a JSON string" in {
      generate(Left("woo")).must(be("\"woo\""))
    }

     "is parsable from a JSON string as an Either[String, Int]" in {
      parse[Either[String, Int]]("\"woo\"").must(be(Left("woo")))
    }
  }

  "A Right[String]" - {
     "generates a JSON string" in {
      generate(Right("woo")).must(be("\"woo\""))
    }

     "is parsable from a JSON string as an Either[Int, String]" in {
      parse[Either[Int, String]]("\"woo\"").must(be(Right("woo")))
    }
  }

  "A JsonNode" - {
     "generates whatever the JsonNode is" in {
      generate(new IntNode(2)).must(be("2"))
    }

     "is parsable from a JSON AST node" in {
      parse[JsonNode]("2").must(be(new IntNode(2)))
    }

     "is parsable from a JSON AST node as a specific type" in {
      parse[IntNode]("2").must(be(new IntNode(2)))
    }

     "is itself parsable" in {
      parse[Int](new IntNode(2)).must(be(2))
    }
  }

  "An Array[Int]" - {
     "generates a JSON array of ints" in {
      generate(Array(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Array[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Array[Int]]("[]").toList.must(be(List.empty))
    }
  }
}
