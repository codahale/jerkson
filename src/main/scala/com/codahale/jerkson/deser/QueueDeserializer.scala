package com.codahale.jerkson.deser

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import scala.collection.mutable.Queue

// TODO: 6/2/11 <coda> -- replace QueueDeserializer with a SeqDeserializer when we drop 2.8.1

/**
 * We only need this because Queue has no generic companion in 2.8.1.
 */
@JsonCachable
class QueueDeserializer(elementType: JavaType,
                        elementDeserializer: JsonDeserializer[Object]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val list = new Queue[Object]

    if (jp.getCurrentToken != JsonToken.START_ARRAY) {
      throw ctxt.mappingException(elementType.getRawClass)
    }

    while (jp.nextToken() != JsonToken.END_ARRAY) {
      list += elementDeserializer.deserialize(jp, ctxt).asInstanceOf[Object]
    }

    list
  }
}
