package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import collection.generic.MapFactory
import collection.MapLike
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer

class ImmutableMapDeserializer[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](companion: MapFactory[CC],
                                                                          valueType: JavaType)
  extends JsonDeserializer[Object] with ResolvableDeserializer {

  var valueDeserializer: JsonDeserializer[Object] = _

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): CC[String, Object] = {
    val builder = companion.newBuilder[String, Object]

    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken != JsonToken.FIELD_NAME &&
        jp.getCurrentToken != JsonToken.END_OBJECT) {
      throw ctxt.mappingException(valueType.getRawClass)
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
