package com.codahale.jerkson.deser

import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{JsonDeserializer, DeserializationContext}
import collection.generic.GenericCompanion

class SeqDeserializer[+CC[X] <: Traversable[X]](companion: GenericCompanion[CC],
                                                elementType: JavaType,
                                                elementDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val builder = companion.newBuilder[Object]

    if (jp.getCurrentToken != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      builder += elementDeserializer.deserialize(jp, ctxt).asInstanceOf[Object]
    }

    builder.result()
  }
}
