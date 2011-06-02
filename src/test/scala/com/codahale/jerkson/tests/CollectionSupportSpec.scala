package com.codahale.jerkson.tests

import scala.collection._
import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec

class CollectionSupportSpec extends Spec {
  class `A BitSet` {
    def `generates a JSON array of ints` = {
      generate(BitSet(1)) must beEqualTo("[1]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[BitSet]("[1,2,3]") must beEqualTo(BitSet(1, 2, 3))
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
      parse[Traversable[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Traversable[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }

  class `A BufferedIterator[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).iterator.buffered) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[BufferedIterator[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[BufferedIterator[Int]]("[]").toList must beEqualTo(List.empty)
    }
  }

  class `An Iterable[Int]` {
    def `generates a JSON array of ints` = {
      generate(Seq(1, 2, 3).toIterable) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[Iterable[Int]]("[1,2,3]").toList must beEqualTo(List(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[Iterable[Int]]("[]").toList must beEqualTo(List.empty)
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

  class `A SortedMap[String, Int]` {
    def `generates a JSON object with int field values` = {
      generate(SortedMap("one" -> 1, "two" -> 2)) must beEqualTo("""{"one":1,"two":2}""")
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize SortedMap instances

    /**
     * I'm not entirely sure I can do this, since I need an Ordering instance
     * before I can do this. I'd need to go from the intended element type to
     * the Ordering instance, but that's done via the implicit scoping, which I
     * can't do with just a Class[_] instance.
     */

    def `is parsable from a JSON object with int field values` = {
//      parse[SortedMap[String, Int]]("""{"one":1,"two":2}""") must beEqualTo(SortedMap("one" -> 1, "two" -> 2))
      pending
    }

    def `is parsable from an empty JSON object` = {
//      parse[SortedMap[String, Int]]("{}") must beEqualTo(SortedMap.empty[String, Int])
      pending
    }
  }

  class `A SortedSet[Int]` {
    def `generates a JSON array of ints` = {
      generate(SortedSet(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize SortedMap instances

    /**
     * I'm not entirely sure I can do this, since I need an Ordering instance
     * before I can do this. I'd need to go from the intended element type to
     * the Ordering instance, but that's done via the implicit scoping, which I
     * can't do with just a Class[_] instance.
     */

    def `is parsable from a JSON array of ints` = {
//      parse[SortedSet[Int]]("[1,2,3]") must beEqualTo(SortedSet(1, 2, 3))
      pending
    }

    def `is parsable from an empty JSON array` = {
//      parse[SortedSet[Int]]("[]") must beEqualTo(SortedSet.empty[String])
      pending
    }
  }
}
