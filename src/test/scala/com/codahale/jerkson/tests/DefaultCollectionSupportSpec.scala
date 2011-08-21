package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import com.codahale.simplespec.annotation.test

class DefaultCollectionSupportSpec extends Spec {
  class `A Range` {
    @test def `generates a JSON object` = {
      generate(Range.inclusive(1, 4, 3)).mustEqual("""{"start":1,"end":4,"step":3,"inclusive":true}""")
    }

    @test def `generates a JSON object without the inclusive field if it's exclusive` = {
      generate(Range(1, 4, 3)).mustEqual("""{"start":1,"end":4,"step":3}""")
    }

    @test def `generates a JSON object without the step field if it's 1` = {
      generate(Range(1, 4)).mustEqual("""{"start":1,"end":4}""")
    }

    @test def `is parsable from a JSON object` = {
      parse[Range]("""{"start":1,"end":4,"step":3,"inclusive":true}""").mustEqual(Range.inclusive(1, 4, 3))
    }

    @test def `is parsable from a JSON object without the inclusive field` = {
      parse[Range]("""{"start":1,"end":4,"step":3}""").mustEqual(Range(1, 4, 3))
    }

    @test def `is parsable from a JSON object without the step field` = {
      parse[Range]("""{"start":1,"end":4}""").mustEqual(Range(1, 4))
    }

    @test def `is not parsable from a JSON object without the required fields` = {
      parse[Range]("""{"start":1}""").mustThrowA[ParsingException]("""Invalid JSON. Needed [start, end, <step>, <inclusive>], but found [start].""")
    }

  }

  class `A Pair[Int]` {
    @test def `generates a two-element JSON array of ints` = {
      pending() // TODO: 5/31/11 <coda> -- fix Pair serialization
      generate(Pair(1, 2)).mustEqual("[1,2]")
    }

    @test def `is parsable from a two-element JSON array of ints` = {
      pending() // TODO: 5/31/11 <coda> -- fix Pair deserialization
      parse[Pair[Int, Int]]("[1,2]").mustEqual(Pair(1, 2))
    }
  }

  class `A Triple[Int]` {
    @test def `generates a three-element JSON array of ints` = {
      pending() // TODO: 5/31/11 <coda> -- fix Triple serialization
      generate(Triple(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a three-element JSON array of ints` = {
      pending() // TODO: 5/31/11 <coda> -- fix Triple deserialization
      parse[Triple[Int, Int, Int]]("[1,2,3]").mustEqual(Triple(1, 2, 3))
    }
  }

  class `A four-tuple` {
    @test def `generates a four-element JSON array` = {
      pending() // TODO: 5/31/11 <coda> -- fix Tuple4 serialization
      generate((1, "2", 3, "4")).mustEqual("[1,\"2\",3,\"4\"]")
    }

    @test def `is parsable from a three-element JSON array of ints` = {
      pending() // TODO: 5/31/11 <coda> -- fix Tuple4 deserialization
      parse[(Int, String, Int, String)]("[1,\"2\",3,\"4\"]").mustEqual((1, "2", 3, "4"))
    }
  }

  // TODO: 6/1/11 <coda> -- add support for all Tuple1->TupleBillionty types

  class `A Seq[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Seq[Int]]("[1,2,3]").mustEqual(Seq(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Seq[Int]]("[]").mustEqual(Seq.empty)
    }
  }

  class `A List[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(List(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[List[Int]]("[1,2,3]").mustEqual(List(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[List[Int]]("[]").mustEqual(List.empty)
    }
  }

  class `An IndexedSeq[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(IndexedSeq(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[IndexedSeq[Int]]("[1,2,3]").mustEqual(IndexedSeq(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[IndexedSeq[Int]]("[]").mustEqual(IndexedSeq.empty)
    }
  }

  class `A Vector[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Vector(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Vector[Int]]("[1,2,3]").mustEqual(Vector(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Vector[Int]]("[]").mustEqual(Vector.empty)
    }
  }

  class `A Set[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Set(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Set[Int]]("[1,2,3]").mustEqual(Set(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Set[Int]]("[]").mustEqual(Set.empty)
    }
  }

  class `A Map[String, Int]` {
    @test def `generates a JSON object with int field values` = {
      generate(Map("one" -> 1, "two" -> 2)).mustEqual("""{"one":1,"two":2}""")
    }

    @test def `is parsable from a JSON object with int field values` = {
      parse[Map[String, Int]]("""{"one":1,"two":2}""").mustEqual(Map("one" -> 1, "two" -> 2))
    }

    @test def `is parsable from an empty JSON object` = {
      parse[Map[String, Int]]("{}").mustEqual(Map.empty)
    }
  }

  class `A Map[String, Any]` {
    @test def `generates a JSON object with mixed field values` = {
      generate(Map("one" -> 1, "two" -> "2")).mustEqual("""{"one":1,"two":"2"}""")
    }

    @test def `is parsable from a JSON object with mixed field values` = {
      parse[Map[String, Object]]("""{"one":1,"two":"2"}""").mustEqual(Map("one" -> 1, "two" -> "2"))
    }

    @test def `is parsable from an empty JSON object` = {
      parse[Map[String, Object]]("{}").mustEqual(Map.empty)
    }
  }

  class `A Stream[Int]` {
    @test def `generates a JSON array` = {
      generate(Stream(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Stream[Int]]("[1,2,3]").mustEqual(Stream(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Stream[Int]]("[]").mustEqual(Stream.empty)
    }
  }

  class `An Iterator[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Iterator[Int]]("[1,2,3]").toList.mustEqual(List(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Iterator[Int]]("[]").toList.mustEqual(List.empty)
    }
  }

  class `A Traversable[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toTraversable).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Traversable[Int]]("[1,2,3]").toList.mustEqual(List(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Traversable[Int]]("[]").toList.mustEqual(List.empty)
    }
  }

  class `A BufferedIterator[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator.buffered).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[BufferedIterator[Int]]("[1,2,3]").toList.mustEqual(List(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[BufferedIterator[Int]]("[]").toList.mustEqual(List.empty)
    }
  }

  class `An Iterable[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toIterable).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Iterable[Int]]("[1,2,3]").toList.mustEqual(List(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Iterable[Int]]("[]").toList.mustEqual(List.empty)
    }
  }
}
