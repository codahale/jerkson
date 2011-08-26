package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import org.junit.{Ignore, Test}

class DefaultCollectionSupportSpec extends Spec {
  class `A Range` {
    @Test def `generates a JSON object` = {
      generate(Range.inclusive(1, 4, 3)).must(be("""{"start":1,"end":4,"step":3,"inclusive":true}"""))
    }

    @Test def `generates a JSON object without the inclusive field if it's exclusive` = {
      generate(Range(1, 4, 3)).must(be("""{"start":1,"end":4,"step":3}"""))
    }

    @Test def `generates a JSON object without the step field if it's 1` = {
      generate(Range(1, 4)).must(be("""{"start":1,"end":4}"""))
    }

    @Test def `is parsable from a JSON object` = {
      parse[Range]("""{"start":1,"end":4,"step":3,"inclusive":true}""").must(be(Range.inclusive(1, 4, 3)))
    }

    @Test def `is parsable from a JSON object without the inclusive field` = {
      parse[Range]("""{"start":1,"end":4,"step":3}""").must(be(Range(1, 4, 3)))
    }

    @Test def `is parsable from a JSON object without the step field` = {
      parse[Range]("""{"start":1,"end":4}""").must(be(Range(1, 4)))
    }

    @Test def `is not parsable from a JSON object without the required fields` = {
      evaluating {
        parse[Range]("""{"start":1}""")
      }.must(throwA[ParsingException]("""Invalid JSON. Needed [start, end, <step>, <inclusive>], but found [start]."""))
    }

  }

  class `A Pair[Int]` {
    @Ignore @Test def `generates a two-element JSON array of ints` = {
      // TODO: 5/31/11 <coda> -- fix Pair serialization
      generate(Pair(1, 2)).must(be("[1,2]"))
    }

    @Ignore @Test def `is parsable from a two-element JSON array of ints` = {
      // TODO: 5/31/11 <coda> -- fix Pair deserialization
      parse[Pair[Int, Int]]("[1,2]").must(be(Pair(1, 2)))
    }
  }

  class `A Triple[Int]` {
    @Ignore @Test def `generates a three-element JSON array of ints` = {
      // TODO: 5/31/11 <coda> -- fix Triple serialization
      generate(Triple(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Ignore @Test def `is parsable from a three-element JSON array of ints` = {
      // TODO: 5/31/11 <coda> -- fix Triple deserialization
      parse[Triple[Int, Int, Int]]("[1,2,3]").must(be(Triple(1, 2, 3)))
    }
  }

  class `A four-tuple` {
    @Ignore @Test def `generates a four-element JSON array` = {
      // TODO: 5/31/11 <coda> -- fix Tuple4 serialization
      generate((1, "2", 3, "4")).must(be("[1,\"2\",3,\"4\"]"))
    }

    @Ignore @Test def `is parsable from a three-element JSON array of ints` = {
      // TODO: 5/31/11 <coda> -- fix Tuple4 deserialization
      parse[(Int, String, Int, String)]("[1,\"2\",3,\"4\"]").must(be((1, "2", 3, "4")))
    }
  }

  // TODO: 6/1/11 <coda> -- add support for all Tuple1->TupleBillionty types

  class `A Seq[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Seq[Int]]("[1,2,3]").must(be(Seq(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Seq[Int]]("[]").must(be(Seq.empty[Int]))
    }
  }

  class `A List[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(List(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[List[Int]]("[1,2,3]").must(be(List(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[List[Int]]("[]").must(be(List.empty[Int]))
    }
  }

  class `An IndexedSeq[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(IndexedSeq(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[IndexedSeq[Int]]("[1,2,3]").must(be(IndexedSeq(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[IndexedSeq[Int]]("[]").must(be(IndexedSeq.empty[Int]))
    }
  }

  class `A Vector[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Vector(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Vector[Int]]("[1,2,3]").must(be(Vector(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Vector[Int]]("[]").must(be(Vector.empty[Int]))
    }
  }

  class `A Set[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Set(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Set[Int]]("[1,2,3]").must(be(Set(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Set[Int]]("[]").must(be(Set.empty[Int]))
    }
  }

  class `A Map[String, Int]` {
    @Test def `generates a JSON object with int field values` = {
      generate(Map("one" -> 1, "two" -> 2)).must(be("""{"one":1,"two":2}"""))
    }

    @Test def `is parsable from a JSON object with int field values` = {
      parse[Map[String, Int]]("""{"one":1,"two":2}""").must(be(Map("one" -> 1, "two" -> 2)))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[Map[String, Int]]("{}").must(be(Map.empty[String, Int]))
    }
  }

  class `A Map[String, Any]` {
    @Test def `generates a JSON object with mixed field values` = {
      generate(Map("one" -> 1, "two" -> "2")).must(be("""{"one":1,"two":"2"}"""))
    }

    @Test def `is parsable from a JSON object with mixed field values` = {
      parse[Map[String, Any]]("""{"one":1,"two":"2"}""").must(be(Map[String, Any]("one" -> 1, "two" -> "2")))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[Map[String, Any]]("{}").must(be(Map.empty[String, Any]))
    }
  }

  class `A Stream[Int]` {
    @Test def `generates a JSON array` = {
      generate(Stream(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Stream[Int]]("[1,2,3]").must(be(Stream(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Stream[Int]]("[]").must(be(Stream.empty[Int]))
    }
  }

  class `An Iterator[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Iterator[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Iterator[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }

  class `A Traversable[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toTraversable).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Traversable[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Traversable[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }

  class `A BufferedIterator[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator.buffered).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[BufferedIterator[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[BufferedIterator[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }

  class `An Iterable[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toIterable).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Iterable[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Iterable[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }
}
