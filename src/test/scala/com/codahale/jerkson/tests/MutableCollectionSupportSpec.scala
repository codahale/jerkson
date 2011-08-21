package com.codahale.jerkson.tests

import com.codahale.simplespec.Spec
import com.codahale.jerkson.Json._
import scala.collection.mutable._
import com.codahale.jerkson.ParsingException
import com.codahale.simplespec.annotation.test

class MutableCollectionSupportSpec extends Spec {
  class `A mutable.ResizableArray[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(ResizableArray(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[ResizableArray[Int]]("[1,2,3]").mustEqual(ResizableArray(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[ResizableArray[Int]]("[]").mustEqual(ResizableArray.empty)
    }
  }

  class `A mutable.ArraySeq[Int]` {
    @test def `generates a JSON array of ints` = {
      generate(ArraySeq(1, 2, 3)).mustEqual("[1,2,3]")
    }


    @test def `is parsable from a JSON array of ints` = {
      parse[ArraySeq[Int]]("[1,2,3]").mustEqual(ArraySeq(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[ArraySeq[Int]]("[]").mustEqual(ArraySeq.empty)
    }
  }

  class `A mutable.MutableList[Int]` {
    private val xs = new MutableList[Int]
    xs ++= List(1, 2, 3)

    @test def `generates a JSON array` = {
      generate(xs).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[MutableList[Int]]("[1,2,3]").mustEqual(xs)
    }

    @test def `is parsable from an empty JSON array` = {
      parse[MutableList[Int]]("[]").mustEqual(new MutableList[Int]())
    }
  }

  class `A mutable.Queue[Int]` {
    @test def `generates a JSON array` = {
      generate(Queue(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[Queue[Int]]("[1,2,3]").mustEqual(Queue(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[Queue[Int]]("[]").mustEqual(new Queue[Int]())
    }
  }

  class `A mutable.ListBuffer[Int]` {
    @test def `generates a JSON array` = {
      generate(ListBuffer(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[ListBuffer[Int]]("[1,2,3]").mustEqual(ListBuffer(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[ListBuffer[Int]]("[]").mustEqual(ListBuffer.empty)
    }
  }

  class `A mutable.ArrayBuffer[Int]` {
    @test def `generates a JSON array` = {
      generate(ArrayBuffer(1, 2, 3)).mustEqual("[1,2,3]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[ArrayBuffer[Int]]("[1,2,3]").mustEqual(ArrayBuffer(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[ArrayBuffer[Int]]("[]").mustEqual(ArrayBuffer.empty)
    }
  }

  class `A mutable.BitSet` {
    @test def `generates a JSON array` = {
      generate(BitSet(1)).mustEqual("[1]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[BitSet]("[1,2,3]").mustEqual(BitSet(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[BitSet]("[]").mustEqual(BitSet.empty)
    }
  }

  class `A mutable.HashSet[Int]` {
    @test def `generates a JSON array` = {
      generate(HashSet(1)).mustEqual("[1]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[HashSet[Int]]("[1,2,3]").mustEqual(HashSet(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[HashSet[Int]]("[]").mustEqual(HashSet.empty)
    }
  }

  class `A mutable.LinkedHashSet[Int]` {
    @test def `generates a JSON array` = {
      generate(LinkedHashSet(1)).mustEqual("[1]")
    }

    @test def `is parsable from a JSON array of ints` = {
      parse[LinkedHashSet[Int]]("[1,2,3]").mustEqual(LinkedHashSet(1, 2, 3))
    }

    @test def `is parsable from an empty JSON array` = {
      parse[LinkedHashSet[Int]]("[]").mustEqual(LinkedHashSet.empty)
    }
  }

  class `A mutable.Map[String, Int]` {
    @test def `generates a JSON object` = {
      generate(Map("one" -> 1)).mustEqual("""{"one":1}""")
    }

    @test def `is parsable from a JSON object with int field values` = {
      parse[Map[String, Int]]("""{"one":1}""").mustEqual(Map("one" -> 1))
    }

    @test def `is parsable from an empty JSON object` = {
      parse[Map[String, Int]]("{}").mustEqual(Map.empty)
    }
  }

  class `A mutable.Map[String, Any]` {
    @test def `is not parsable from an empty JSON object in a JSON array` = {
      parse[Map[String, Any]]("[{}]").mustThrowA[ParsingException]
    }
  }

  class `A mutable.HashMap[String, Int]` {
    @test def `generates a JSON object` = {
      generate(HashMap("one" -> 1)).mustEqual("""{"one":1}""")
    }

    @test def `is parsable from a JSON object with int field values` = {
      parse[HashMap[String, Int]]("""{"one":1}""").mustEqual(HashMap("one" -> 1))
    }

    @test def `is parsable from an empty JSON object` = {
      parse[HashMap[String, Int]]("{}").mustEqual(HashMap.empty)
    }
  }

  class `A mutable.LinkedHashMap[String, Int]` {
    @test def `generates a JSON object` = {
      generate(LinkedHashMap("one" -> 1)).mustEqual("""{"one":1}""")
    }

    @test def `is parsable from a JSON object with int field values` = {
      parse[LinkedHashMap[String, Int]]("""{"one":1}""").mustEqual(LinkedHashMap("one" -> 1))
    }

    @test def `is parsable from an empty JSON object` = {
      parse[LinkedHashMap[String, Int]]("{}").mustEqual(LinkedHashMap.empty)
    }
  }
}
