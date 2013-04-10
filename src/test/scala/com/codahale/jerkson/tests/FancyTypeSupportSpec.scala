package com.codahale.jerkson.tests

import java.net.URI
import com.codahale.jerkson.Json._
import java.util.UUID
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class FancyTypeSupportSpec extends FreeSpec with MustMatchers {
  "A URI" - {
     "generates a JSON string" in {
      generate(new URI("http://example.com/resource?query=yes")).must(be("\"http://example.com/resource?query=yes\""))
    }

     "is parsable from a JSON string" in {
      parse[URI]("\"http://example.com/resource?query=yes\"").must(be(new URI("http://example.com/resource?query=yes")))
    }
  }

  "A UUID" - {
    val uuid = UUID.fromString("a62047e4-bfb5-4d71-aad7-1a6b338eee63")

     "generates a JSON string" in {
      generate(uuid).must(be("\"a62047e4-bfb5-4d71-aad7-1a6b338eee63\""))
    }

     "is parsable from a JSON string" in {
      parse[UUID]("\"a62047e4-bfb5-4d71-aad7-1a6b338eee63\"").must(be(uuid))
    }
  }
}
