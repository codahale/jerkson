package com.codahale.jerkson

import java.io.IOException
import org.codehaus.jackson.map.JsonMappingException
import org.codehaus.jackson.{JsonParseException, JsonProcessingException}

object ParsingException {
  def apply(cause: JsonProcessingException): ParsingException = {
    val message = cause match {
      case e: JsonMappingException => {
        if (e.getMessage.contains("deserialize instance of")) {
          "Invalid JSON."
        } else {
          e.getMessage
        }
      }
      case e: JsonParseException => {
        val fake = new JsonParseException("", e.getLocation)
        val msg = e.getMessage.replace(fake.getMessage, "").replaceAll(""" (\(from.*\))""", "")
        "Malformed JSON. %s at character offset %d.".format(msg, e.getLocation.getCharOffset)
      }
    }
    new ParsingException(message, cause)
  }
}

class ParsingException(message: String, cause: Throwable)
        extends IOException(message, cause)
