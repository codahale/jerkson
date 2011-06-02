package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.Json._
import scala.collection.mutable._

class MutableCollectionSupportSpec extends Spec {
  class `A ResizableArray[Int]` {
    def `generates a JSON array of ints` = {
      generate(ResizableArray(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ResizableArray deserialization
//      parse[ResizableArray[Int]]("[1,2,3]") must beEqualTo(ResizableArray(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ResizableArray deserialization
//      parse[ResizableArray[Int]]("[]") must beEqualTo(ResizableArray.empty)
    }
  }

  class `An ArraySeq[Int]` {
    def `generates a JSON array of ints` = {
      generate(ArraySeq(1, 2, 3)) must beEqualTo("[1,2,3]")
    }


    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ArraySeq deserialization
//      parse[ArraySeq[Int]]("[1,2,3]") must beEqualTo(ArraySeq(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ArraySeq deserialization
//      parse[ArraySeq[Int]]("[]") must beEqualTo(ArraySeq.empty)
    }
  }

  class `A MutableList[Int]` {
    private val xs = new MutableList[Int]
    xs ++= List(1, 2, 3)

    def `generates a JSON array` = {
      generate(xs) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.MutableList deserialization
//      parse[MutableList[Int]]("[1,2,3]") must beEqualTo(xs)
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.MutableList deserialization
//      parse[MutableList[Int]]("[]") must beEqualTo(MutableList.empty)
    }
  }

  class `A Queue[Int]` {
    def `generates a JSON array` = {
      generate(Queue(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.Queue deserialization
//      parse[Queue[Int]]("[1,2,3]") must beEqualTo(Queue(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.Queue deserialization
//      parse[Queue[Int]]("[]") must beEqualTo(Queue.empty)
    }
  }

  class `A ListBuffer[Int]` {
    def `generates a JSON array` = {
      generate(ListBuffer(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ListBuffer deserialization
//      parse[ListBuffer[Int]]("[1,2,3]") must beEqualTo(ListBuffer(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ListBuffer deserialization
//      parse[ListBuffer[Int]]("[]") must beEqualTo(ListBuffer.empty)
    }
  }

  class `An ArrayBuffer[Int]` {
    def `generates a JSON array` = {
      generate(ArrayBuffer(1, 2, 3)) must beEqualTo("[1,2,3]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ArrayBuffer deserialization
//      parse[ArrayBuffer[Int]]("[1,2,3]") must beEqualTo(ArrayBuffer(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.ArrayBuffer deserialization
//      parse[ArrayBuffer[Int]]("[]") must beEqualTo(ArrayBuffer.empty)
    }
  }

  class `A HashSet[Int]` {
    def `generates a JSON array` = {
      generate(HashSet(1)) must beEqualTo("[1]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.HashSet deserialization
//      parse[HashSet[Int]]("[1,2,3]") must beEqualTo(HashSet(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.HashSet deserialization
//      parse[HashSet[Int]]("[]") must beEqualTo(HashSet.empty)
    }
  }

  class `A LinkedHashSet[Int]` {
    def `generates a JSON array` = {
      generate(LinkedHashSet(1)) must beEqualTo("[1]")
    }

    def `is parsable from a JSON array of ints` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.LinkedHashSet deserialization
//      parse[LinkedHashSet[Int]]("[1,2,3]") must beEqualTo(LinkedHashSet(1, 2, 3))
    }

    def `is parsable from an empty JSON array` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.LinkedHashSet deserialization
//      parse[LinkedHashSet[Int]]("[]") must beEqualTo(LinkedHashSet.empty)
    }
  }

  class `A HashMap[String, Int]` {
    def `generates a JSON object` = {
      generate(HashMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }

    def `is parsable from a JSON object with int field values` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.HashMap deserialization
//      parse[mutable.HashMap[String, Int]]("""{"one":1}""") must
//        beEqualTo(mutable.HashMap("one" -> 1))
    }

    def `is parsable from an empty JSON object` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.HashMap deserialization
//      parse[HashMap[String, Int]]("{}") must beEqualTo(HashMap.empty)
    }
  }

  class `A LinkedHashMap[String, Int]` {
    def `generates a JSON object` = {
      generate(LinkedHashMap("one" -> 1)) must beEqualTo("""{"one":1}""")
    }

    def `is parsable from a JSON object with int field values` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.LinkedHashMap deserialization
//      parse[LinkedHashMap[String, Int]]("""{"one":1}""") must
//        beEqualTo(LinkedHashMap("one" -> 1))
    }

    def `is parsable from an empty JSON object` = {
      pending // TODO: 5/31/11 <coda> -- fix mutable.LinkedHashMap deserialization
//      parse[LinkedHashMap[String, Int]]("{}") must beEqualTo(LinkedHashMap.empty)
    }
  }
}
