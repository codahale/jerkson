package com.codahale.jerkson.deser

import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{JsonDeserializer, DeserializationContext}
import collection.generic.GenericCompanion
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer

class SeqDeserializer[+CC[X] <: Traversable[X]](companion: GenericCompanion[CC],
                                                elementType: JavaType)
  extends JsonDeserializer[Object] with ResolvableDeserializer {

  var elementDeserializer: JsonDeserializer[Object] = _

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): CC[Object] = {
    val builder = companion.newBuilder[Object]

    if (jp.getCurrentToken != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      builder += elementDeserializer.deserialize(jp, ctxt)
    }

    builder.result()
  }

  def resolve(ctxt: DeserializationContext) {
    elementDeserializer = ctxt.findRootValueDeserializer(elementType)
  }

  override def isCachable = true
}
