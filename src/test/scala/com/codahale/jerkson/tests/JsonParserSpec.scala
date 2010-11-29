package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.JsonParser.parse

object JsonParserSpec extends Spec {
  class `Parsing a JSON int` {
    val parser = parse("1")

    def `should be readable as an Int` {
      parser.readValueAs[Int] must beEqualTo(1)
    }

    def `should be readable as an Option[Int]` {
      parser.readValueAs[Option[Int]] must beSome(1)
    }

    def `should be readable as a Long` {
      parser.readValueAs[Long] must beEqualTo(1L)
    }
  }

  class `Parsing a JSON float` {
    val parser = parse("1.1")

    def `should be readable as a Float` {
      parser.readValueAs[Float] must beEqualTo(1.1F)
    }

    def `should be readable as a Double` {
      parser.readValueAs[Double] must beEqualTo(1.1)
    }
  }

  class `Parsing a JSON string` {
    val parser = parse("\"woo\"")

    def `should be readable as a String` {
      parser.readValueAs[String] must beEqualTo("woo")
    }
  }

  class `Parsing a JSON null` {
    val parser = parse("null")

    def `should be readable as a null Object` {
      parser.readValueAs[String] must beNull
    }

    def `should be readable as an Option[_]` {
      parser.readValueAs[Option[String]] must beNone
    }
  }

  class `Parsing a JSON array of ints` {
    val parser = parse("[1,2,3,4]")

    def `should be readable as a Seq[Int]` {
      parser.readValueAs[Seq[Int]] must beEqualTo(Seq(1, 2, 3, 4))
    }

    def `should be readable as a IndexedSeq[Int]` {
      parser.readValueAs[IndexedSeq[Int]] must beEqualTo(IndexedSeq(1, 2, 3, 4))
    }

    def `should be readable as a List[Int]` {
      parser.readValueAs[List[Int]] must beEqualTo(List(1, 2, 3, 4))
    }

    def `should be readable as a Vector[Int]` {
      parser.readValueAs[Vector[Int]] must beEqualTo(Vector(1, 2, 3, 4))
    }
  }

  class `Parsing a JSON array of ints with nulls` {
    val parser = parse("[1,2,null,4]")

    def `should be readable as a List[Option[Int]]` {
      parser.readValueAs[List[Option[Int]]] must beEqualTo(List(Some(1),
                                                                Some(2),
                                                                None,
                                                                Some(4)))
    }
  }

  class `Parsing a JSON object with int field values` {
    val parser = parse(""" {"one":1, "two": 2} """)

    def `should be readable as a Map[String, Int]` {
      parser.readValueAs[Map[String, Int]] must beEqualTo(Map("one" -> 1,
                                                              "two" -> 2))
    }
  }

  class `Parsing a JSON object as a case class` {
    def `should use all available constructors` {
      parse(""" {"id":1, "name": "Coda"} """)
        .readValueAs[Person] must beEqualTo(Person(1, "Coda"))

      parse(""" {"id":1, "firstName": "Coda", "lastName": "Hale"} """)
        .readValueAs[Person] must beEqualTo(Person(1, "Coda Hale"))
    }

    def `should handle missing Option members` {
      parse(""" {"one": "1"} """)
        .readValueAs[ClassWithOption] must beEqualTo(ClassWithOption("1", None))

      parse(""" {"one": "1", "two": "2"} """)
        .readValueAs[ClassWithOption] must beEqualTo(ClassWithOption("1", Some("2")))
    }
  }
}

case class Person(id: Long, name: String) {
  def this(id: Long, firstName: String, lastName: String) = this(id, firstName + " " + lastName)
}

case class ClassWithOption(one: String, two: Option[String])
