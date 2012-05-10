package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.core.{JsonToken, JsonParser}

class OptionDeserializer(elementType: JavaType,
                              elementDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  override def getEmptyValue = None

  override def getNullValue = None

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == JsonToken.VALUE_NULL) {
      None
    } else {
      Some(elementDeserializer.deserialize(jp, ctxt))
    }
  }
}
