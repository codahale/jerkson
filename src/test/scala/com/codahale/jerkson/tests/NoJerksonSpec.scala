package com.codahale.jerkson.tests

import com.codahale.jerkson.Json._
import com.codahale.jerkson.ParsingException
import com.codahale.simplespec.Spec
import org.junit.Test

class NoJerksonSpec extends Spec {
  class `A no-jerkson case class` {
    @Test def `generates a JSON object with matching field values` = {
      generate(NoJerksonCaseClass(1, "Coda")).must(be("""{"ID":1,"name-thing":"Coda","meta":[1,"Coda"]}"""))
    }

    @Test def `is parsable from a JSON object with corresponding fields` = {
      parse[NoJerksonCaseClass]("""{"ID":1,"name-thing":"Coda"}""").must(be(NoJerksonCaseClass(1, "Coda")))
      parse[NoJerksonCaseClass]("""{"ID":1,"name-thing":"Coda","meta":[1,"Coda"]}""").must(be(NoJerksonCaseClass(1, "Coda")))
    }

    @Test def `is not parsable from a JSON object with extra fields (using default jackson options)` = {
      evaluating {
        parse[NoJerksonCaseClass]("""{"ID":1,"name-thing":"Coda","meta":[2,"Not Coda"],"derp":100}""")
      }.must(throwA[ParsingException])
    }

    @Test def `is not parsable from an incomplete JSON object` = {
      evaluating {
        parse[NoJerksonCaseClass]("""{"ID":1}""")
      }.must(throwA[ParsingException])
    }
  }

  class `A no-jerkson iterator` {
    @Test def `generates a JSON object with matching field values` = {
      generate(new NoJerksonIterator(3)).must(be("""{"count":3}"""))
    }

    @Test def `is parsable from a JSON object with corresponding fields` = {
      parse[NoJerksonIterator]("""{"count":3}""").must(beA[NoJerksonIterator])
      parse[NoJerksonIterator]("""{"count":3}""").count.must(be(3))
    }

    @Test def `is not parsable from a JSON object with extra fields (using default jackson options)` = {
      evaluating {
        parse[NoJerksonIterator]("""{"count":3,"derp":100}""")
      }.must(throwA[ParsingException])
    }

    @Test def `is not parsable from an incomplete JSON object` = {
      evaluating {
        parse[NoJerksonIterator]("""{}""")
      }.must(throwA[ParsingException])
    }
  }
}
