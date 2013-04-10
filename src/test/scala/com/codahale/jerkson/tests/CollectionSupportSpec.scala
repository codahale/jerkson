package com.codahale.jerkson.tests

import scala.collection._
import com.codahale.jerkson.Json._
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class CollectionSupportSpec extends FreeSpec with MustMatchers {
  "A collection.BitSet" - {
     "generates a JSON array of ints" in {
      generate(BitSet(1)).must(be("[1]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[BitSet]("[1,2,3]").must(be(BitSet(1, 2, 3)))
    }
  }

  "A collection.Iterator[Int]" - {
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

  "A collection.Traversable[Int]" - {
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

  "A collection.BufferedIterator[Int]" - {
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

  "A collection.Iterable[Int]" - {
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

  "A collection.Set[Int]" - {
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

  "A collection.Map[String, Int]" - {
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

  "A collection.IndexedSeq[Int]" - {
     "generates a JSON array of ints" in {
      generate(IndexedSeq(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[IndexedSeq[Int]]("[1,2,3]").must(be(IndexedSeq(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[IndexedSeq[Int]]("[]").must(be(IndexedSeq.empty))
    }
  }

  "A collection.Seq[Int]" - {
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

  "A collection.SortedMap[String, Int]" - {
     "generates a JSON object with int field values" in {
      generate(SortedMap("one" -> 1, "two" -> 2)).must(be("""{"one":1,"two":2}"""))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize SortedMap instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */
    
    "is parsable from a JSON object with int field values" ignore {
      parse[SortedMap[String, Int]]("""{"one":1,"two":2}""").must(be(SortedMap("one" -> 1, "two" -> 2)))
    }

    "is parsable from an empty JSON object" ignore {
      parse[SortedMap[String, Int]]("{}").must(be(SortedMap.empty[String, Int]))
    }
  }

  "A collection.SortedSet[Int]" - {
     "generates a JSON array of ints" in {
      generate(SortedSet(1, 2, 3)).must(be("[1,2,3]"))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize SortedMap instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */

    "is parsable from a JSON array of ints" ignore {
      parse[SortedSet[Int]]("[1,2,3]").must(be(SortedSet(1, 2, 3)))

    }

    "is parsable from an empty JSON array" ignore {
      parse[SortedSet[Int]]("[]").must(be(SortedSet.empty[Int]))
    }
  }
}
