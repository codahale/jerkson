package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}

class IteratorDeserializer(elementType: JavaType,
                           elementDeserializer: JsonDeserializer[Object]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val builder = Seq.newBuilder[Object]

    if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      builder += elementDeserializer.deserialize(jp, ctxt).asInstanceOf[Object]
    }

    builder.result.iterator
  }
}
