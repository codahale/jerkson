package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.Json._
import scala.collection.immutable._

class ImmutableCollectionSupportSpec extends Spec {
  class `An immutable.Seq[Int]` {
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

  class `An immutable.List[Int]` {
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

  class `An immutable.IndexedSeq[Int]` {
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

  class `An immutable.TreeSet[Int]` {
    def `generates a JSON array` = {
      generate(TreeSet(1)) must beEqualTo("[1]")
    }

    /**
     * I'm not entirely sure I can do this, since I need an Ordering instance
     * before I can do this. I'd need to go from the intended element type to
     * the Ordering instance, but that's done via the implicit scoping, which I
     * can't do with just a Class[_] instance.
     */

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix immutable.TreeSet deserialization
//      parse[TreeSet[Int]]("[1,2,3]") must beEqualTo(TreeSet(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix immutable.TreeSet deserialization
//      parse[TreeSet[Int]]("[]") must beEqualTo(TreeSet.empty[Int])
    }
  }

  class `An immutable.HashSet[Int]` {
    def `generates a JSON array` = {
      generate(HashSet(1)) must beEqualTo("[1]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[HashSet[Int]]("[1,2,3]") must beEqualTo(HashSet(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[HashSet[Int]]("[]") must beEqualTo(HashSet.empty)
    }
  }

  class `An immutable.BitSet` {
    def `generates a JSON array` = {
      generate(BitSet(1)) must beEqualTo("[1]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[BitSet]("[1,2,3]") must beEqualTo(BitSet(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      parse[BitSet]("[]") must beEqualTo(BitSet.empty)
    }
  }

  class `An immutable.TreeMap[String, Int]` {
    def `generates a JSON object` = {
      generate(TreeMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }

    def `is parsable from a JSON object with int field values` = {
      pending // TODO: 5/31/11 <coda> -- fix immutable.TreeMap deserialization
//      parse[TreeMap[String, Int]]("""{"one":1}""") must beEqualTo(TreeMap("one" -> 1))
    }

    def `is parsable from an empty JSON object` = {
      pending // TODO: 5/31/11 <coda> -- fix immutable.TreeMap deserialization
//      parse[TreeMap[String, Int]]("{}") must beEqualTo(TreeMap.empty[String, Int])
    }
  }

  class `An immutable.HashMap[String, Int]` {
    def `generates a JSON object` = {
      generate(HashMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }

    def `is parsable from a JSON object with int field values` = {
      parse[HashMap[String, Int]]("""{"one":1}""") must beEqualTo(HashMap("one" -> 1))
    }

    def `is parsable from an empty JSON object` = {
      parse[HashMap[String, Int]]("{}") must beEqualTo(HashMap.empty)
    }
  }
}
