package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import scala.collection.mutable._
import com.codahale.jerkson.ParsingException
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class MutableCollectionSupportSpec extends FreeSpec with MustMatchers {
  "A mutable.ResizableArray[Int]" - {
     "generates a JSON array of ints" in {
      generate(ResizableArray(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[ResizableArray[Int]]("[1,2,3]").must(be(ResizableArray(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[ResizableArray[Int]]("[]").must(be(ResizableArray.empty[Int]))
    }
  }

  "A mutable.ArraySeq[Int]" - {
    "generates a JSON array of ints" in {
      generate(ArraySeq(1, 2, 3)).must(be("[1,2,3]"))
    }


    "is parsable from a JSON array of ints" in {
      parse[ArraySeq[Int]]("[1,2,3]") == ArraySeq(1, 2, 3) must be (true)
    }

    "is parsable from an empty JSON array" in {
      parse[ArraySeq[Int]]("[]") == ArraySeq.empty[Int] must be (true)
    }
  }

  "A mutable.MutableList[Int]" - {
    val xs = new MutableList[Int]
    xs ++= List(1, 2, 3)

     "generates a JSON array" in {
      generate(xs).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[MutableList[Int]]("[1,2,3]").must(be(xs))
    }

     "is parsable from an empty JSON array" in {
      parse[MutableList[Int]]("[]").must(be(new MutableList[Int]()))
    }
  }

  "A mutable.Queue[Int]" - {
     "generates a JSON array" in {
      generate(Queue(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[Queue[Int]]("[1,2,3]").must(be(Queue(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[Queue[Int]]("[]").must(be(new Queue[Int]()))
    }
  }

  "A mutable.ListBuffer[Int]" - {
     "generates a JSON array" in {
      generate(ListBuffer(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[ListBuffer[Int]]("[1,2,3]").must(be(ListBuffer(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[ListBuffer[Int]]("[]").must(be(ListBuffer.empty[Int]))
    }
  }

  "A mutable.ArrayBuffer[Int]" - {
     "generates a JSON array" in {
      generate(ArrayBuffer(1, 2, 3)).must(be("[1,2,3]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[ArrayBuffer[Int]]("[1,2,3]").must(be(ArrayBuffer(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[ArrayBuffer[Int]]("[]").must(be(ArrayBuffer.empty[Int]))
    }
  }

  "A mutable.BitSet" - {
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

  "A mutable.HashSet[Int]" - {
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

  "A mutable.LinkedHashSet[Int]" - {
     "generates a JSON array" in {
      generate(LinkedHashSet(1)).must(be("[1]"))
    }

     "is parsable from a JSON array of ints" in {
      parse[LinkedHashSet[Int]]("[1,2,3]").must(be(LinkedHashSet(1, 2, 3)))
    }

     "is parsable from an empty JSON array" in {
      parse[LinkedHashSet[Int]]("[]").must(be(LinkedHashSet.empty[Int]))
    }
  }

  "A mutable.Map[String, Int]" - {
     "generates a JSON object" in {
      generate(Map("one" -> 1)).must(be("""{"one":1}"""))
    }

     "is parsable from a JSON object with int field values" in {
      parse[Map[String, Int]]("""{"one":1}""").must(be(Map("one" -> 1)))
    }

     "is parsable from an empty JSON object" in {
      parse[Map[String, Int]]("{}").must(be(Map.empty[String, Int]))
    }
  }

  "A mutable.Map[String, Any]" - {
     "is not parsable from an empty JSON object in a JSON array" in {
      evaluating {
        parse[Map[String, Any]]("[{}]")
      }.must(produce[ParsingException])
    }
  }

  "A mutable.HashMap[String, Int]" - {
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

  "A mutable.LinkedHashMap[String, Int]" - {
     "generates a JSON object" in {
      generate(LinkedHashMap("one" -> 1)).must(be("""{"one":1}"""))
    }

     "is parsable from a JSON object with int field values" in {
      parse[LinkedHashMap[String, Int]]("""{"one":1}""").must(be(LinkedHashMap("one" -> 1)))
    }

     "is parsable from an empty JSON object" in {
      parse[LinkedHashMap[String, Int]]("{}").must(be(LinkedHashMap.empty[String, Int]))
    }
  }
}
