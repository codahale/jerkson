package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.simplespec.Spec
import com.codahale.jerkson.ParsingException
import org.codehaus.jackson.node.IntNode
import org.junit.Test

class EitherSupportSpec extends Spec {
  class `An Either of two case classes` {
    @Test def `is parseable when the Left is used` = {
      val e = parse[Either[CaseClassWithArrays, CaseClass]]("""{"one": "a", "two": [ "b", "c" ], "three": [ 0, 1 ]}""")

      e.isLeft.must(be(true))

      val c = e.left.get
      c.one.must(be("a"))
      c.two.must(be(Array[String]("b", "c")))
      c.three.must(be(Array[Int](0, 1)))
    }

    @Test def `is parseable when the Right is used` = {
      val e = parse[Either[CaseClass, CaseClassWithArrays]]("""{"one": "a", "two": [ "b", "c" ], "three": [ 0, 1 ]}""")

      e.isRight.must(be(true))

      val c = e.right.get
      c.one.must(be("a"))
      c.two.must(be(Array[String]("b", "c")))
      c.three.must(be(Array[Int](0, 1)))
    }
  }
}
