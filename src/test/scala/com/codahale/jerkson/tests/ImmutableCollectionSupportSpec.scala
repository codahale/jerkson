package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import scala.collection.immutable._
import com.codahale.jerkson.ParsingException
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class ImmutableCollectionSupportSpec extends FreeSpec with MustMatchers {
  "An immutable.Seq[Int]" - {
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

  "An immutable.List[Int]" - {
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

  "An immutable.IndexedSeq[Int]" - {
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

  "An immutable.TreeSet[Int]" - {
     "generates a JSON array" in {
      generate(TreeSet(1)).must(be("[1]"))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize TreeSet instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */
    
    "is parsable from a JSON array of ints" ignore {
      parse[TreeSet[Int]]("[1,2,3]").must(be(TreeSet(1, 2, 3)))
    }

    "is parsable from an empty JSON array" ignore {
      parse[TreeSet[Int]]("[]").must(be(TreeSet.empty[Int]))
    }
  }

  "An immutable.HashSet[Int]" - {
     "generates a JSON array" in {
      generate(HashSet(1)).must(be("[1]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[HashSet[Int]]("[1,2,3]").must(be(HashSet(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[HashSet[Int]]("[]").must(be(HashSet.empty[Int]))
    }
  }

  "An immutable.BitSet" - {
     "generates a JSON array" in {
      generate(BitSet(1)).must(be("[1]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[BitSet]("[1,2,3]").must(be(BitSet(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[BitSet]("[]").must(be(BitSet.empty))
    }
  }

  "An immutable.TreeMap[String, Int]" - {
     "generates a JSON object" in {
      generate(TreeMap("one" -> 1)).must(be("""{"one":1}"""))
    }

    // TODO: 6/1/11 <coda> -- figure out how to deserialize TreeMap instances

    /**
     * I think all this would take is a mapping from Class[_] to Ordering, which
     * would need to have hard-coded the various primitive types, and then add
     * support for Ordered and Comparable classes. Once we have the Ordering,
     * we can pass it in manually to a builder.
     */
    
    "is parsable from a JSON object with int field values" ignore {
      parse[TreeMap[String, Int]]("""{"one":1}""").must(be(TreeMap("one" -> 1)))
    }

    "is parsable from an empty JSON object" ignore {
      parse[TreeMap[String, Int]]("{}").must(be(TreeMap.empty[String, Int]))
    }
  }

  "An immutable.HashMap[String, Int]" - {
     "generates a JSON object" in {
      generate(HashMap("one" -> 1)).must(be("""{"one":1}"""))
    }

     "is parsable from a JSON object with int field values" in {
      parse[HashMap[String, Int]]("""{"one":1}""").must(be(HashMap("one" -> 1)))
    }

     "is parsable from an empty JSON object" in {
      parse[HashMap[String, Int]]("{}").must(be(HashMap.empty[String, Int]))
    }
  }

  "An immutable.HashMap[String, Any]" - {
     "generates a JSON object" in {
      generate(HashMap[String, Any]("one" -> 1)).must(be("""{"one":1}"""))
    }

     "is parsable from a JSON object with int field values" in {
      parse[HashMap[String, Any]]("""{"one":1}""").must(be(HashMap("one" -> 1)))
    }

     "is parsable from an empty JSON object" in {
      parse[HashMap[String, Any]]("{}").must(be(HashMap.empty[String, Any]))
    }

     "is not parsable from an empty JSON object in a JSON array" in {
      evaluating {
        parse[HashMap[String, Any]]("[{}]")
      }.must(produce[ParsingException])
    }
  }

  "An immutable.Map[Int, String]" - {
     "generates a JSON object" in {
      generate(Map(1 -> "one")).must(be("""{"1":"one"}"""))
    }

     "is parsable from a JSON object with decimal field names and string field values" in {
      parse[Map[Int, String]]("""{"1":"one"}""").must(be(Map(1 -> "one")))
    }

     "is not parsable from a JSON object with non-decimal field names" in {
      evaluating {
        parse[Map[Int, String]]("""{"one":"one"}""")
      }.must(produce[ParsingException])
    }

     "is parsable from an empty JSON object" in {
      parse[Map[Int, String]]("{}").must(be(Map.empty[Int, String]))
    }
  }

  "An immutable.Map[Int, Any]" - {
     "is not parsable from an empty JSON object in a JSON array" in {
      evaluating {
        parse[Map[Int, Any]]("[{}]")
      }.must(produce[ParsingException])
    }
  }

  "An immutable.IntMap[Any]" - {
     "is not parsable from an empty JSON object in a JSON array" in {
      evaluating {
        parse[IntMap[Any]]("[{}]")
      }.must(produce[ParsingException])
    }
  }

  "An immutable.LongMap[Any]" - {
     "is not parsable from an empty JSON object in a JSON array" in {
      evaluating {
        parse[LongMap[Any]]("[{}]")
      }.must(produce[ParsingException])
    }
  }

  "An immutable.Map[Long, Any]" - {
     "is not parsable from an empty JSON object in a JSON array" in {
      evaluating {
        parse[Map[Long, Any]]("[{}]")
      }.must(produce[ParsingException])
    }
  }

  "An immutable.Map[Long, String]" - {
     "generates a JSON object" in {
      generate(Map(1L -> "one")).must(be("""{"1":"one"}"""))
    }

     "is parsable from a JSON object with decimal field names and string field values" in {
      parse[Map[Long, String]]("""{"1":"one"}""").must(be(Map(1L -> "one")))
    }

     "is not parsable from a JSON object with non-decimal field names" in {
      evaluating {
        parse[Map[Long, String]]("""{"one":"one"}""")
      }.must(produce[ParsingException])
    }

     "is parsable from an empty JSON object" in {
      parse[Map[Long, String]]("{}").must(be(Map.empty[Long, String]))
    }
  }

  "An immutable.IntMap[String]" - {
     "generates a JSON object" in {
      generate(IntMap(1 -> "one")).must(be("""{"1":"one"}"""))
    }

     "is parsable from a JSON object with decimal field names and string field values" in {
      parse[IntMap[String]]("""{"1":"one"}""").must(be(IntMap(1 -> "one")))
    }

     "is not parsable from a JSON object with non-decimal field names" in {
      evaluating {
        parse[IntMap[String]]("""{"one":"one"}""")
      }.must(produce[ParsingException])
    }

     "is parsable from an empty JSON object" in {
      parse[IntMap[String]]("{}").must(be(IntMap.empty[String]))
    }
  }

  "An immutable.LongMap[String]" - {
     "generates a JSON object" in {
      generate(LongMap(1L -> "one")).must(be("""{"1":"one"}"""))
    }

     "is parsable from a JSON object with int field names and string field values" in {
      parse[LongMap[String]]("""{"1":"one"}""").must(be(LongMap(1L -> "one")))
    }

     "is not parsable from a JSON object with non-decimal field names" in {
      evaluating {
        parse[LongMap[String]]("""{"one":"one"}""")
      }.must(produce[ParsingException])
    }

     "is parsable from an empty JSON object" in {
      parse[LongMap[String]]("{}").must(be(LongMap.empty))
    }
  }

  "An immutable.Queue[Int]" - {
     "generates a JSON array" in {
      generate(Queue(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Queue[Int]]("[1,2,3]").must(be(Queue(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Queue[Int]]("[]").must(be(Queue.empty))
    }
  }
}
