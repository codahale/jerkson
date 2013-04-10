package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.ParsingException
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class DefaultCollectionSupportSpec extends FreeSpec with MustMatchers {
  "A Range" - {
     "generates a JSON object" in {
      generate(Range.inclusive(1, 4, 3)).must(be("""{"start":1,"end":4,"step":3,"inclusive":true}"""))
    }

     "generates a JSON object without the inclusive field if it's exclusive" in {
      generate(Range(1, 4, 3)).must(be("""{"start":1,"end":4,"step":3}"""))
    }

     "generates a JSON object without the step field if it's 1" in {
      generate(Range(1, 4)).must(be("""{"start":1,"end":4}"""))
    }

     "is parsable from a JSON object" in {
      parse[Range]("""{"start":1,"end":4,"step":3,"inclusive":true}""").must(be(Range.inclusive(1, 4, 3)))
    }

     "is parsable from a JSON object without the inclusive field" in {
      parse[Range]("""{"start":1,"end":4,"step":3}""").must(be(Range(1, 4, 3)))
    }

     "is parsable from a JSON object without the step field" in {
      parse[Range]("""{"start":1,"end":4}""").must(be(Range(1, 4)))
    }

     "is not parsable from a JSON object without the required fields" in {
      evaluating {
        parse[Range]("""{"start":1}""")
      }.must(produce[ParsingException])
    }

  }

  "A Pair[Int]" - {
    "generates a two-element JSON array of ints" ignore {
      // TODO: 5/31/11 <coda> -- fix Pair serialization
      generate(Pair(1, 2)).must(be("[1,2]"))
    }

    "is parsable from a two-element JSON array of ints" ignore {
      // TODO: 5/31/11 <coda> -- fix Pair deserialization
      parse[Pair[Int, Int]]("[1,2]").must(be(Pair(1, 2)))
    }
  }

  "A Triple[Int]" - {
    "generates a three-element JSON array of ints" ignore {
      // TODO: 5/31/11 <coda> -- fix Triple serialization
      generate(Triple(1, 2, 3)).must(be("[1,2,3]"))
    }

    "is parsable from a three-element JSON array of ints" ignore {
      // TODO: 5/31/11 <coda> -- fix Triple deserialization
      parse[Triple[Int, Int, Int]]("[1,2,3]").must(be(Triple(1, 2, 3)))
    }
  }

  "A four-tuple" - {
    "generates a four-element JSON array" ignore {
      // TODO: 5/31/11 <coda> -- fix Tuple4 serialization
      generate((1, "2", 3, "4")).must(be("[1,\"2\",3,\"4\"]"))
    }

    "is parsable from a three-element JSON array of ints" ignore {
      // TODO: 5/31/11 <coda> -- fix Tuple4 deserialization
      parse[(Int, String, Int, String)]("[1,\"2\",3,\"4\"]").must(be((1, "2", 3, "4")))
    }
  }

  // TODO: 6/1/11 <coda> -- add support for all Tuple1->TupleBillionty types

  "A Seq[Int]" - {
     "generates a JSON array of ints" in {
      generate(Seq(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Seq[Int]]("[1,2,3]").must(be(Seq(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Seq[Int]]("[]").must(be(Seq.empty[Int]))
    }
  }

  "A List[Int]" - {
     "generates a JSON array of ints" in {
      generate(List(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[List[Int]]("[1,2,3]").must(be(List(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[List[Int]]("[]").must(be(List.empty[Int]))
    }
  }

  "An IndexedSeq[Int]" - {
     "generates a JSON array of ints" in {
      generate(IndexedSeq(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[IndexedSeq[Int]]("[1,2,3]").must(be(IndexedSeq(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[IndexedSeq[Int]]("[]").must(be(IndexedSeq.empty[Int]))
    }
  }

  "A Vector[Int]" - {
     "generates a JSON array of ints" in {
      generate(Vector(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Vector[Int]]("[1,2,3]").must(be(Vector(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Vector[Int]]("[]").must(be(Vector.empty[Int]))
    }
  }

  "A Set[Int]" - {
     "generates a JSON array of ints" in {
      generate(Set(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Set[Int]]("[1,2,3]").must(be(Set(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Set[Int]]("[]").must(be(Set.empty[Int]))
    }
  }

  "A Map[String, Int]" - {
     "generates a JSON object with int field values" in {
      generate(Map("one" -> 1, "two" -> 2)).must(be("""{"one":1,"two":2}"""))
    }

     "is parsable from a JSON object with int field values" in {
      parse[Map[String, Int]]("""{"one":1,"two":2}""").must(be(Map("one" -> 1, "two" -> 2)))
    }

     "is parsable from an empty JSON object" in {
      parse[Map[String, Int]]("{}").must(be(Map.empty[String, Int]))
    }
  }

  "A Map[String, Any]" - {
     "generates a JSON object with mixed field values" in {
      generate(Map("one" -> 1, "two" -> "2")).must(be("""{"one":1,"two":"2"}"""))
    }

     "is parsable from a JSON object with mixed field values" in {
      parse[Map[String, Any]]("""{"one":1,"two":"2"}""").must(be(Map[String, Any]("one" -> 1, "two" -> "2")))
    }

     "is parsable from an empty JSON object" in {
      parse[Map[String, Any]]("{}").must(be(Map.empty[String, Any]))
    }
  }

  "A Stream[Int]" - {
     "generates a JSON array" in {
      generate(Stream(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Stream[Int]]("[1,2,3]").must(be(Stream(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Stream[Int]]("[]").must(be(Stream.empty[Int]))
    }
  }

  "An Iterator[Int]" - {
     "generates a JSON array of ints" in {
      generate(Seq(1, 2, 3).iterator).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Iterator[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Iterator[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }

  "A Traversable[Int]" - {
     "generates a JSON array of ints" in {
      generate(Seq(1, 2, 3).toTraversable).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Traversable[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Traversable[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }

  "A BufferedIterator[Int]" - {
     "generates a JSON array of ints" in {
      generate(Seq(1, 2, 3).iterator.buffered).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[BufferedIterator[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[BufferedIterator[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }

  "An Iterable[Int]" - {
     "generates a JSON array of ints" in {
      generate(Seq(1, 2, 3).toIterable).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Iterable[Int]]("[1,2,3]").toList.must(be(List(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Iterable[Int]]("[]").toList.must(be(List.empty[Int]))
    }
  }
}
