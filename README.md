Jerkson
-------

*Because I think you should use JSON.*

Jerkson is a Scala wrapper for [Jackson](http://jackson.codehaus.org/) which
brings Scala's ease-of-use to Jackson's features.


Requirements
------------

* Scala 2.8.0 or 2.8.1
* Jackson 1.6.2


Setting Up Your Project
-----------------------

In your [simple-build-tool](http://code.google.com/p/simple-build-tool/) project
file, add Jerkson as a dependency:
    
    val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
    val jerkson = "com.codahale" %% "jerkson" % "0.0.1" withSources()


Parsing JSON
------------

    import com.codahale.jerkson.Json._
    
    // Parse JSON arrays
    parse[List[Int]]("[1,2,3]") //=> List(1,2,3)
    
    // Parse JSON objects
    parse[Map[String, Int]]("""{"one":1,"two":2}""") //=> Map("one"->1,"two"->2)
    
    // Parse JSON objects as case classes
    case class Person(id: Long, name: String)
    parse[Person]("""{"id":1,"name":"Coda"}""") //=> Person(1,"Coda")
    
    // Parse streaming arrays of things
    parseStreamOf[Person](inputStream) { p =>
      println("New person: " + p)
    }

For more examples, check out the [parsing specs](blob/master/src/test/scala/com/codahale/jerkson/tests/JsonParsingSpec.scala).


Generating JSON
---------------

    // Generate JSON arrays
    generate(List(1, 2, 3)) //=> [1,2,3]
    
    // Generate JSON objects
    generate(Map("one"->1, "two"->"dos")) //=> {"one":1,"two":"dos"}

For more examples, check out the [generating specs](blob/master/src/test/scala/com/codahale/jerkson/tests/JsonGeneratingSpec.scala).


License
-------

Copyright (c) 2010 Coda Hale

Published under The MIT License, see LICENSE