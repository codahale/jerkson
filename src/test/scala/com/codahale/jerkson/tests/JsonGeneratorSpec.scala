package com.codahale.jerkson.tests

import com.codahale.jerkson.JsonGenerator._
import com.codahale.simplespec.Spec

object JsonGeneratorSpec extends Spec {
  class `An Int` {
    def `should generate a JSON int` {
      generate(15) must beEqualTo("15")
    }
  }

  class `A Long` {
    def `should generate a JSON int` {
      generate(15L) must beEqualTo("15")
    }
  }

  class `A Float` {
    def `should generate a JSON float` {
      generate(15.1F) must beEqualTo("15.1")
    }
  }

  class `A Double` {
    def `should generate a JSON float` {
      generate(15.1) must beEqualTo("15.1")
    }
  }

  class `A String` {
    def `should generate a JSON string` {
      generate("woo") must beEqualTo("\"woo\"")
    }
  }

  class `A null Object` {
    def `should generate a JSON null` {
      generate[Object](null) must beEqualTo("null")
    }
  }

  class `A Boolean` {
    def `should generate a JSON true` {
      generate(true) must beEqualTo("true")
    }

    def `should generate a JSON false` {
      generate(false) must beEqualTo("false")
    }
  }

  class `A Seq[Int]` {
    def `should generate a JSON array of ints` {
      generate(Seq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A List[Int]` {
    def `should generate a JSON array of ints` {
      generate(List(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A IndexedSeq[Int]` {
    def `should generate a JSON array of ints` {
      generate(IndexedSeq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A Vector[Int]` {
    def `should generate a JSON array of ints` {
      generate(Vector(1, 2, 3)) must beEqualTo("[1,2,3]")
    }
  }

  class `A Map[String, Int]` {
    def `should generate a JSON object with int field values` {
      generate(Map("one" -> 1, "two" -> 2)) must beEqualTo("""{"one":1,"two":2}""")
    }
  }

  class `A Map[String, Any]` {
    def `should generate a JSON object with mixed field values` {
      generate(Map("one" -> 1, "two" -> "2")) must beEqualTo("""{"one":1,"two":"2"}""")
    }
  }

  class `A case class` {
    def `should generate a JSON object with matching field values` {
      generate(Person(1, "Coda")) must beEqualTo("""{"id":1,"name":"Coda"}""")
    }
  }
}
