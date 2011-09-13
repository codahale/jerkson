package com.codahale.jerkson.tests

import java.net.URI
import com.codahale.simplespec.Spec
import org.junit.Test
import com.codahale.jerkson.Json._
import java.util.UUID

class FancyTypeSupportSpec extends Spec {
  class `A URI` {
    @Test def `generates a JSON string` = {
      generate(new URI("http://example.com/resource?query=yes")).must(be("\"http://example.com/resource?query=yes\""))
    }

    @Test def `is parsable from a JSON string` = {
      parse[URI]("\"http://example.com/resource?query=yes\"").must(be(new URI("http://example.com/resource?query=yes")))
    }
  }

  class `A UUID` {
    val uuid = UUID.fromString("a62047e4-bfb5-4d71-aad7-1a6b338eee63")

    @Test def `generates a JSON string` = {
      generate(uuid).must(be("\"a62047e4-bfb5-4d71-aad7-1a6b338eee63\""))
    }

    @Test def `is parsable from a JSON string` = {
      parse[UUID]("\"a62047e4-bfb5-4d71-aad7-1a6b338eee63\"").must(be(uuid))
    }
  }
}
