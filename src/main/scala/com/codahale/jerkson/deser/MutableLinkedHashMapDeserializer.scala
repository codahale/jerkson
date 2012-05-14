package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import scala.collection.mutable
import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer

class MutableLinkedHashMapDeserializer(valueType: JavaType) extends JsonDeserializer[Object] with ResolvableDeserializer {
  var valueDeserializer: JsonDeserializer[Object] = _

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val builder = mutable.LinkedHashMap.newBuilder[String, Object]

    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    while (jp.getCurrentToken != JsonToken.END_OBJECT) {
      val name = jp.getCurrentName
      jp.nextToken()
      builder += ((name, valueDeserializer.deserialize(jp, ctxt)))
      jp.nextToken()
    }

    builder.result()
  }

  def resolve(ctxt: DeserializationContext) {
    valueDeserializer = ctxt.findRootValueDeserializer(valueType)
  }

  override def isCachable = true
}
