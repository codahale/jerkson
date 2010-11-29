package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class OptionDeserializer(elementType: JavaType,
                              elementDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == JsonToken.VALUE_NULL) {
      None
    } else {
      Some(elementDeserializer.deserialize(jp, ctxt))
    }
  }
}
