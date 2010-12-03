package com.codahale.jerkson.deser

import collection.mutable.Builder
import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{JsonDeserializer, DeserializationContext}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class SeqDeserializer(newBuilder: => Builder[Object, Object],
                      elementType: JavaType,
                      elementDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val builder = newBuilder

    if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      builder += elementDeserializer.deserialize(jp, ctxt).asInstanceOf[Object]
    }

    builder.result
  }
}
