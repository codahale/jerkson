package com.codahale.jerkson

import org.codehaus.jackson.map.{MappingJsonFactory, ObjectMapper}
import org.codehaus.jackson.{JsonGenerator, JsonParser => JacksonParser}

object Json extends Parser with Generator {
  protected val mapper = new ObjectMapper
  mapper.registerModule(new ScalaModule)

  protected val factory = new MappingJsonFactory(mapper)
  factory.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)
  factory.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
  factory.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES)
  factory.enable(JacksonParser.Feature.ALLOW_COMMENTS)
  factory.enable(JacksonParser.Feature.AUTO_CLOSE_SOURCE)
}
