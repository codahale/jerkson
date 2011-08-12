package com.codahale.jerkson.tests

import java.net.URI
import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import com.codahale.jerkson.Json._
import java.util.UUID

class FancyTypeSupportSpec extends Spec {
  class `A URI` {
    @test def `generates a JSON string` = {
      generate(new URI("http://example.com/resource?query=yes")) must
        beEqualTo("\"http://example.com/resource?query=yes\"")
    }

    @test def `is parsable from a JSON string` = {
      parse[URI]("\"http://example.com/resource?query=yes\"") must
        beEqualTo(new URI("http://example.com/resource?query=yes"))
    }
  }

  class `A UUID` {
    val uuid = UUID.fromString("a62047e4-bfb5-4d71-aad7-1a6b338eee63")

    @test def `generates a JSON string` = {
      generate(uuid) must beEqualTo("\"a62047e4-bfb5-4d71-aad7-1a6b338eee63\"")
    }

    @test def `is parsable from a JSON string` = {
      parse[UUID]("\"a62047e4-bfb5-4d71-aad7-1a6b338eee63\"") must beEqualTo(uuid)
    }
  }
}
