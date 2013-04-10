package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import java.io.ByteArrayInputStream
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class StreamingSpec extends FreeSpec with MustMatchers {
  "Parsing a stream of objects" - {
    val json = """[
      {"id":1, "name": "Coda"},
      {"id":2, "name": "Niki"},
      {"id":3, "name": "Biscuit"},
      {"id":4, "name": "Louie"}
    ]"""

     "returns an iterator of stream elements" in {
      stream[CaseClass](new ByteArrayInputStream(json.getBytes)).toList
        .must(be(CaseClass(1, "Coda") :: CaseClass(2, "Niki") ::
                  CaseClass(3, "Biscuit") :: CaseClass(4, "Louie") :: Nil))
    }
  }
}
