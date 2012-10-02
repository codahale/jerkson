package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer

class OptionDeserializer(elementType: JavaType)
  extends JsonDeserializer[Object] with ResolvableDeserializer {

  var elementDeserializer: JsonDeserializer[Object] = _

  override def getEmptyValue = None

  override def getNullValue = None

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == JsonToken.VALUE_NULL) {
      None
    } else {
      Some(elementDeserializer.deserialize(jp, ctxt))
    }
  }

  def resolve(ctxt: DeserializationContext) {
    elementDeserializer = ctxt.findRootValueDeserializer(elementType)
  }

  override def isCachable = true
}
