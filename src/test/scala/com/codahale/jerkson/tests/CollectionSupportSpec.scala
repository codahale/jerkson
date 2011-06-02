package com.codahale.jerkson.tests

import scala.collection._
import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec

class CollectionSupportSpec extends Spec {
  class `A collection.BitSet` {
    def `generates a JSON array of ints` = {
      generate(BitSet(1)) must beEqualTo("[1]")
    }

    def `is parsable from a JSON array of ints` = {
      parse[BitSet]("[1,2,3]") must beEqualTo(BitSet(1, 2, 3))
    }
  }

  // TODO: 6/1/11 <coda> -- add test for collection.BufferedIterator
  // TODO: 6/1/11 <coda> -- add test for collection.IndexedSeq
  // TODO: 6/1/11 <coda> -- add test for collection.Iterable
  // TODO: 6/1/11 <coda> -- add test for collection.Iterator
  // TODO: 6/1/11 <coda> -- add test for collection.Map
  // TODO: 6/1/11 <coda> -- add test for collection.Seq
  // TODO: 6/1/11 <coda> -- add test for collection.Set
  // TODO: 6/1/11 <coda> -- add test for collection.SortedMap
  // TODO: 6/1/11 <coda> -- add test for collection.SortedSet
  // TODO: 6/1/11 <coda> -- add test for collection.Traversable
}
