package com.codahale.jerkson

import java.io.IOException
import org.codehaus.jackson.map.JsonMappingException
import org.codehaus.jackson.{JsonParseException, JsonProcessingException}

object ParsingException {
  def format(cause: JsonProcessingException) = cause match {
    case e: JsonMappingException => {
      "Invalid JSON."
    }
    case e: JsonParseException => {
      val fake = new JsonParseException("", e.getLocation)
      val msg = e.getMessage.replace(fake.getMessage, "").replaceAll(""" (\(from.*\))""", "")
      "Malformed JSON. %s at character offset %d.".format(msg, e.getLocation.getCharOffset)
    }
  }
}

class ParsingException(cause: JsonProcessingException)
        extends IOException(ParsingException.format(cause))
