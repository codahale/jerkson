package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.Json._
import scala.collection.mutable._
import com.codahale.jerkson.ParsingException
import org.junit.Test

class MutableCollectionSupportSpec extends Spec {
  class `A mutable.ResizableArray[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(ResizableArray(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[ResizableArray[Int]]("[1,2,3]").must(be(ResizableArray(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[ResizableArray[Int]]("[]").must(be(ResizableArray.empty[Int]))
    }
  }

  class `A mutable.ArraySeq[Int]` {
    @Test def `generates a JSON array of ints` = {
      generate(ArraySeq(1, 2, 3)).must(be("[1,2,3]"))
    }


    @Test def `is parsable from a JSON array of ints` = {
      parse[ArraySeq[Int]]("[1,2,3]").must(be(ArraySeq(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[ArraySeq[Int]]("[]").must(be(ArraySeq.empty[Int]))
    }
  }

  class `A mutable.MutableList[Int]` {
    private val xs = new MutableList[Int]
    xs ++= List(1, 2, 3)

    @Test def `generates a JSON array` = {
      generate(xs).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[MutableList[Int]]("[1,2,3]").must(be(xs))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[MutableList[Int]]("[]").must(be(new MutableList[Int]()))
    }
  }

  class `A mutable.Queue[Int]` {
    @Test def `generates a JSON array` = {
      generate(Queue(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[Queue[Int]]("[1,2,3]").must(be(Queue(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[Queue[Int]]("[]").must(be(new Queue[Int]()))
    }
  }

  class `A mutable.ListBuffer[Int]` {
    @Test def `generates a JSON array` = {
      generate(ListBuffer(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[ListBuffer[Int]]("[1,2,3]").must(be(ListBuffer(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[ListBuffer[Int]]("[]").must(be(ListBuffer.empty[Int]))
    }
  }

  class `A mutable.ArrayBuffer[Int]` {
    @Test def `generates a JSON array` = {
      generate(ArrayBuffer(1, 2, 3)).must(be("[1,2,3]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[ArrayBuffer[Int]]("[1,2,3]").must(be(ArrayBuffer(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[ArrayBuffer[Int]]("[]").must(be(ArrayBuffer.empty[Int]))
    }
  }

  class `A mutable.BitSet` {
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

  class `A mutable.HashSet[Int]` {
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

  class `A mutable.LinkedHashSet[Int]` {
    @Test def `generates a JSON array` = {
      generate(LinkedHashSet(1)).must(be("[1]"))
    }

    @Test def `is parsable from a JSON array of ints` = {
      parse[LinkedHashSet[Int]]("[1,2,3]").must(be(LinkedHashSet(1, 2, 3)))
    }

    @Test def `is parsable from an empty JSON array` = {
      parse[LinkedHashSet[Int]]("[]").must(be(LinkedHashSet.empty[Int]))
    }
  }

  class `A mutable.Map[String, Int]` {
    @Test def `generates a JSON object` = {
      generate(Map("one" -> 1)).must(be("""{"one":1}"""))
    }

    @Test def `is parsable from a JSON object with int field values` = {
      parse[Map[String, Int]]("""{"one":1}""").must(be(Map("one" -> 1)))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[Map[String, Int]]("{}").must(be(Map.empty[String, Int]))
    }
  }

  class `A mutable.Map[String, Any]` {
    @Test def `is not parsable from an empty JSON object in a JSON array` = {
      evaluating {
        parse[Map[String, Any]]("[{}]")
      }.must(throwA[ParsingException])
    }
  }

  class `A mutable.HashMap[String, Int]` {
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

  class `A mutable.LinkedHashMap[String, Int]` {
    @Test def `generates a JSON object` = {
      generate(LinkedHashMap("one" -> 1)).must(be("""{"one":1}"""))
    }

    @Test def `is parsable from a JSON object with int field values` = {
      parse[LinkedHashMap[String, Int]]("""{"one":1}""").must(be(LinkedHashMap("one" -> 1)))
    }

    @Test def `is parsable from an empty JSON object` = {
      parse[LinkedHashMap[String, Int]]("{}").must(be(LinkedHashMap.empty[String, Int]))
    }
  }
}
