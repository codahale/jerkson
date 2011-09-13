package com.codahale.jerkson.tests

import scala.collection._
import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import org.junit.{Ignore, Test}

class CollectionSupportSpec extends Spec {
  class `A collection.BitSet` {
    @Test def `generates a JSON array of ints` = {
      generate(BitSet(1)).must(be("[1]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[BitSet]("[1,2,3]").must(be(BitSet(1, 2, 3)))
    }
  }

  class `A collection.Iterator[Int]` {
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

  class `A collection.Traversable[Int]` {
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

  class `A collection.BufferedIterator[Int]` {
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

  class `A collection.Iterable[Int]` {
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

  class `A collection.Set[Int]` {
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

  class `A collection.Map[String, Int]` {
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

  class `A collection.IndexedSeq[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(IndexedSeq(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[IndexedSeq[Int]]("[1,2,3]").must(be(IndexedSeq(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[IndexedSeq[Int]]("[]").must(be(IndexedSeq.empty))
    }
  }

  class `A collection.Seq[Int]` {
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

  class `A collection.SortedMap[String, Int]` {
    @Test def `generates a JSON object with int field values` = {
      generate(SortedMap("one" -> 1, "two" -> 2)).must(be("""{"one":1,"two":2}"""))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize SortedMap instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */
    
    @Ignore @Test def `is parsable from a JSON object with int field values` = {
      parse[SortedMap[String, Int]]("""{"one":1,"two":2}""").must(be(SortedMap("one" -> 1, "two" -> 2)))
    }

    @Ignore @Test def `is parsable from an empty JSON object` = {
      parse[SortedMap[String, Int]]("{}").must(be(SortedMap.empty[String, Int]))
    }
  }

  class `A collection.SortedSet[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(SortedSet(1, 2, 3)).must(be("[1,2,3]"))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize SortedMap instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */

    @Ignore @Test def `is parsable from a JSON array of ints` = {
      parse[SortedSet[Int]]("[1,2,3]").must(be(SortedSet(1, 2, 3)))

    }

    @Ignore @Test def `is parsable from an empty JSON array` = {
      parse[SortedSet[Int]]("[]").must(be(SortedSet.empty[Int]))
    }
  }
}
