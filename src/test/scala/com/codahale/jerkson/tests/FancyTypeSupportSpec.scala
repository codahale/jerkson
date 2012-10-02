package com.codahale.jerkson.tests

import java.net.URI
import com.codahale.simplespec.Spec
import org.junit.Test
import org.joda.time.DateTime
import com.codahale.jerkson.Json._
import java.util.UUID

case class LogEntry(when: DateTime, message: String)

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

  class `A Joda DateTime` {
    val ts = new DateTime("1883-07-03")

    @Test def `Generates a JSON int` {
      generate(ts).must(be("-2729606822000"))
    }

    @Test def `Is parsable from a JSON string` = {
      parse[DateTime]("-2729606822000").compareTo(ts).must(be(0))
    }
  }

  class `A Joda DateTime sequence` {
    val ts = Seq(new DateTime("1883-07-03"),
                 new DateTime("1924-06-03"))

    @Test def `Generates a JSON int list` {
      generate(ts).must(be("[-2729606822000,-1438358400000]"))
    }

    @Test def `Is parsable from a JSON string` = {
      val dates = parse[Seq[DateTime]]("[-2729606822000, -1438358400000]")
      dates(0).compareTo(ts(0)).must(be(0))
      dates(1).compareTo(ts(1)).must(be(0))
    }
  }

  class `A case class with a Joda DateTime` {
    val ent = LogEntry(new DateTime("1883-07-03"), "birth")

    @Test def `Generates a JSON object` {
      generate(ent).must(be("""{"when":-2729606822000,"message":"birth"}"""))
    }

    @Test def `Is parsable from a JSON object` = {
      val parsedEnt = parse[LogEntry]("""{"when": -2729606822000, "message": "birth"}""")
      parsedEnt.when.compareTo(ent.when).must(be(0))
      parsedEnt.message.must(be(ent.message))
    }
  }
}
