package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.core.{JsonToken, JsonParser}

class StringBuilderDeserializer extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken != JsonToken.VALUE_STRING) {
      throw ctxt.mappingException(classOf[StringBuilder])
    }

    new StringBuilder(jp.getText)
  }

  override def isCachable = true
}
