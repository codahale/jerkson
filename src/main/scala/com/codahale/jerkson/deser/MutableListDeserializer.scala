package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import scala.collection.mutable.MutableList
import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.map.annotate.JsonCachable

// TODO: 6/2/11 <coda> -- replace MutableListDeserializer with a SeqDeserializer when we drop 2.8.1

/**
 * We only need this because MutableList has no generic companion in 2.8.1.
 */
@JsonCachable
class MutableListDeserializer(elementType: JavaType,
                              elementDeserializer: JsonDeserializer[Object]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val list = new MutableList[Object]

    if (jp.getCurrentToken != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      list += elementDeserializer.deserialize(jp, ctxt).asInstanceOf[Object]
    }

    list
  }
}
