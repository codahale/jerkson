package com.codahale.jerkson.deser

import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{JsonDeserializer, DeserializationContext}
import org.codehaus.jackson.map.annotate.JsonCachable
import collection.generic.GenericCompanion

@JsonCachable
class SeqDeserializer[+CC[X] <: Traversable[X]](companion: GenericCompanion[CC],
                                                elementType: JavaType,
                                                elementDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val builder = companion.newBuilder[Object]

    if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      builder += elementDeserializer.deserialize(jp, ctxt).asInstanceOf[Object]
    }

    builder.result
  }
}
