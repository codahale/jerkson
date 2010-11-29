package com.codahale.jerkson

import java.io.StringWriter

object JsonGenerator {
  import JsonFactory._

  def generate[A](obj: A)(implicit mf: Manifest[A]): String = {
    val writer = new StringWriter
    val generator = factory.createJsonGenerator(writer)
    generator.writeObject(obj)
    generator.close()
    writer.toString
  }
}
