package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec

class DefaultCollectionSupportSpec extends Spec {
  class `A Range` {
    def `generates a JSON object` = {
      pending // TODO: 5/31/11 <coda> -- fix Range serialization
//      generate(Range.inclusive(1, 4, 3)) must
//        beEqualTo("""{"start":1,"end":4,"step":3,"inclusive":true}""")
    }

    def `is parsable from a JSON object` = {
      pending // TODO: 5/31/11 <coda> -- fix Range deserialization
//      parse[Range]("""{"start":1,"end":4,"step":3,"inclusive":true}""") must
//        beEqualTo(Range.inclusive(1, 4, 3))
    }
  }

  class `A Pair[Int]` {
    def `generates a two-element JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix Pair serialization
//      generate(Pair(1, 2)) must beEqualTo("[1,2]")
    }

    def `is parsable from a two-element JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix Pair deserialization
//      parse[Pair[Int, Int]]("[1,2]") must beEqualTo(Pair(1, 2))
    }
  }

  class `A Triple[Int]` {
    def `generates a three-element JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix Triple serialization
//      generate(Triple(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a three-element JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix Triple deserialization
//      parse[Triple[Int, Int, Int]]("[1,2,3]") must beEqualTo(Triple(1, 2, 3))
    }
  }

  class `A four-tuple` {
    def `generates a four-element JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix Tuple4 serialization
//      generate((1, "2", 3, "4")) must beEqualTo("[1,\"2\",3,\"4\"]")
    }

    def `is parsable from a three-element JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix Tuple4 deserialization
//      parse[(Int, String, Int, String)]("[1,\"2\",3,\"4\"]") must beEqualTo((1, "2", 3, "4"))
    }
  }

  // TODO: 6/1/11 <coda> -- add support for all Tuple1->TupleBillionty types

  class `A Seq[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[Seq[Int]]("[1,2,3]") must beEqualTo(Seq(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Seq[Int]]("[]") must beEqualTo(Seq.empty)
    }
  }

  class `A List[Int]` {
    def `generates a JSON array of ints` = {
      generate(List(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[List[Int]]("[1,2,3]") must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[List[Int]]("[]") must beEqualTo(List.empty)
    }
  }

  class `An IndexedSeq[Int]` {
    def `generates a JSON array of ints` = {
      generate(IndexedSeq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[IndexedSeq[Int]]("[1,2,3]") must beEqualTo(IndexedSeq(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[IndexedSeq[Int]]("[]") must beEqualTo(IndexedSeq.empty)
    }
  }

  class `A Vector[Int]` {
    def `generates a JSON array of ints` = {
      generate(Vector(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[Vector[Int]]("[1,2,3]") must beEqualTo(Vector(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Vector[Int]]("[]") must beEqualTo(Vector.empty)
    }
  }

  class `A Set[Int]` {
    def `generates a JSON array of ints` = {
      generate(Set(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[Set[Int]]("[1,2,3]") must beEqualTo(Set(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Set[Int]]("[]") must beEqualTo(Set.empty)
    }
  }

  class `A Map[String, Int]` {
    def `generates a JSON object with int field values` = {
      generate(Map("one" -> 1, "two" -> 2)) must beEqualTo("""{"one":1,"two":2}""")
    }

    def `is parsable from a JSON object with int field values` = {
      parse[Map[String, Int]]("""{"one":1,"two":2}""") must beEqualTo(Map("one" -> 1, "two" -> 2))
    }

    def `is parsable from an empty JSON object` = {
      parse[Map[String, Int]]("{}") must beEqualTo(Map.empty)
    }
  }

  class `A Map[String, Any]` {
    def `generates a JSON object with mixed field values` = {
      generate(Map("one" -> 1, "two" -> "2")) must beEqualTo("""{"one":1,"two":"2"}""")
    }

    def `is parsable from a JSON object with mixed field values` = {
      parse[Map[String, Object]]("""{"one":1,"two":"2"}""") must beEqualTo(Map("one" -> 1, "two" -> "2"))
    }

    def `is parsable from an empty JSON object` = {
      parse[Map[String, Object]]("{}") must beEqualTo(Map.empty)
    }
  }

  class `A Stream[Int]` {
    def `generates a JSON array` = {
      generate(Stream(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix Stream deserialization
//      parse[Stream[Int]]("[1,2,3]") must beEqualTo(Stream(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix Stream deserialization
//      parse[Stream[Int]]("[]") must beEqualTo(Stream.empty)
    }
  }

  class `An Iterator[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[Iterator[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Iterator[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }

  class `A Traversable[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toTraversable) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 6/1/11 <coda> -- fix Traversable deserialization
//      parse[Traversable[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 6/1/11 <coda> -- fix Traversable deserialization
//      parse[Traversable[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }

  class `A BufferedIterator[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator.buffered) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 6/1/11 <coda> -- fix BufferedIterator deserialization
//      parse[BufferedIterator[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 6/1/11 <coda> -- fix BufferedIterator deserialization
//      parse[BufferedIterator[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }

  class `An Iterable[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toIterable) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 6/1/11 <coda> -- fix Iterable deserialization
//      parse[Iterable[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 6/1/11 <coda> -- fix Iterable deserialization
//      parse[Iterable[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }
}
