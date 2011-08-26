package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.Json._
import scala.collection.immutable._
import com.codahale.jerkson.ParsingException
import org.junit.{Ignore, Test}

class ImmutableCollectionSupportSpec extends Spec {
  class `An immutable.Seq[Int]` {
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

  class `An immutable.List[Int]` {
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

  class `An immutable.IndexedSeq[Int]` {
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

  class `An immutable.TreeSet[Int]` {
    @Test def `generates a JSON array` = {
      generate(TreeSet(1)).must(be("[1]"))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize TreeSet instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */
    
    @Ignore @Test def `is parsable from a JSON array of ints` = {
      parse[TreeSet[Int]]("[1,2,3]").must(be(TreeSet(1, 2, 3)))
    }

    @Ignore @Test def `is parsable from an empty JSON array` = {
      parse[TreeSet[Int]]("[]").must(be(TreeSet.empty[Int]))
    }
  }

  class `An immutable.HashSet[Int]` {
    @Test def `generates a JSON array` = {
      generate(HashSet(1)).must(be("[1]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[HashSet[Int]]("[1,2,3]").must(be(HashSet(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[HashSet[Int]]("[]").must(be(HashSet.empty[Int]))
    }
  }

  class `An immutable.BitSet` {
    @Test def `generates a JSON array` = {
      generate(BitSet(1)).must(be("[1]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[BitSet]("[1,2,3]").must(be(BitSet(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[BitSet]("[]").must(be(BitSet.empty))
    }
  }

  class `An immutable.TreeMap[String, Int]` {
    @Test def `generates a JSON object` = {
      generate(TreeMap("one" -> 1)).must(be("""{"one":1}"""))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize TreeMap instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */
    
    @Ignore @Test def `is parsable from a JSON object with int field values` = {
      parse[TreeMap[String, Int]]("""{"one":1}""").must(be(TreeMap("one" -> 1)))
    }

    @Ignore @Test def `is parsable from an empty JSON object` = {
      parse[TreeMap[String, Int]]("{}").must(be(TreeMap.empty[String, Int]))
    }
  }

  class `An immutable.HashMap[String, Int]` {
    @Test def `generates a JSON object` = {
      generate(HashMap("one" -> 1)).must(be("""{"one":1}"""))
    }

    @Test def `is parsable from a JSON object with int field values` = {
      parse[HashMap[String, Int]]("""{"one":1}""").must(be(HashMap("one" -> 1)))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[HashMap[String, Int]]("{}").must(be(HashMap.empty[String, Int]))
    }
  }

  class `An immutable.HashMap[String, Any]` {
    @Test def `generates a JSON object` = {
      generate(HashMap[String, Any]("one" -> 1)).must(be("""{"one":1}"""))
    }

    @Test def `is parsable from a JSON object with int field values` = {
      parse[HashMap[String, Any]]("""{"one":1}""").must(be(HashMap("one" -> 1)))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[HashMap[String, Any]]("{}").must(be(HashMap.empty[String, Any]))
    }

    @Test def `is not parsable from an empty JSON object in a JSON array` = {
      evaluating {
        parse[HashMap[String, Any]]("[{}]")
      }.must(throwA[ParsingException])
    }
  }

  class `An immutable.Map[Int, String]` {
    @Test def `generates a JSON object` = {
      generate(Map(1 -> "one")).must(be("""{"1":"one"}"""))
    }

    @Test def `is parsable from a JSON object with decimal field names and string field values` = {
      parse[Map[Int, String]]("""{"1":"one"}""").must(be(Map(1 -> "one")))
    }

    @Test def `is not parsable from a JSON object with non-decimal field names` = {
      evaluating {
        parse[Map[Int, String]]("""{"one":"one"}""")
      }.must(throwA[ParsingException])
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[Map[Int, String]]("{}").must(be(Map.empty[Int, String]))
    }
  }

  class `An immutable.Map[Int, Any]` {
    @Test def `is not parsable from an empty JSON object in a JSON array` = {
      evaluating {
        parse[Map[Int, Any]]("[{}]")
      }.must(throwA[ParsingException])
    }
  }

  class `An immutable.IntMap[Any]` {
    @Test def `is not parsable from an empty JSON object in a JSON array` = {
      evaluating {
        parse[IntMap[Any]]("[{}]")
      }.must(throwA[ParsingException])
    }
  }

  class `An immutable.LongMap[Any]` {
    @Test def `is not parsable from an empty JSON object in a JSON array` = {
      evaluating {
        parse[LongMap[Any]]("[{}]")
      }.must(throwA[ParsingException])
    }
  }

  class `An immutable.Map[Long, Any]` {
    @Test def `is not parsable from an empty JSON object in a JSON array` = {
      evaluating {
        parse[Map[Long, Any]]("[{}]")
      }.must(throwA[ParsingException])
    }
  }

  class `An immutable.Map[Long, String]` {
    @Test def `generates a JSON object` = {
      generate(Map(1L -> "one")).must(be("""{"1":"one"}"""))
    }

    @Test def `is parsable from a JSON object with decimal field names and string field values` = {
      parse[Map[Long, String]]("""{"1":"one"}""").must(be(Map(1L -> "one")))
    }

    @Test def `is not parsable from a JSON object with non-decimal field names` = {
      evaluating {
        parse[Map[Long, String]]("""{"one":"one"}""")
      }.must(throwA[ParsingException])
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[Map[Long, String]]("{}").must(be(Map.empty[Long, String]))
    }
  }

  class `An immutable.IntMap[String]` {
    @Test def `generates a JSON object` = {
      generate(IntMap(1 -> "one")).must(be("""{"1":"one"}"""))
    }

    @Test def `is parsable from a JSON object with decimal field names and string field values` = {
      parse[IntMap[String]]("""{"1":"one"}""").must(be(IntMap(1 -> "one")))
    }

    @Test def `is not parsable from a JSON object with non-decimal field names` = {
      evaluating {
        parse[IntMap[String]]("""{"one":"one"}""")
      }.must(throwA[ParsingException])
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[IntMap[String]]("{}").must(be(IntMap.empty[String]))
    }
  }

  class `An immutable.LongMap[String]` {
    @Test def `generates a JSON object` = {
      generate(LongMap(1L -> "one")).must(be("""{"1":"one"}"""))
    }

    @Test def `is parsable from a JSON object with int field names and string field values` = {
      parse[LongMap[String]]("""{"1":"one"}""").must(be(LongMap(1L -> "one")))
    }

    @Test def `is not parsable from a JSON object with non-decimal field names` = {
      evaluating {
        parse[LongMap[String]]("""{"one":"one"}""")
      }.must(throwA[ParsingException])
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[LongMap[String]]("{}").must(be(LongMap.empty))
    }
  }

  class `An immutable.Queue[Int]` {
    @Test def `generates a JSON array` = {
      generate(Queue(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Queue[Int]]("[1,2,3]").must(be(Queue(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Queue[Int]]("[]").must(be(Queue.empty))
    }
  }
}
