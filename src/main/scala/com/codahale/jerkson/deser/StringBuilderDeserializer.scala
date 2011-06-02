package com.codahale.jerkson.deser

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}

@JsonCachable
class StringBuilderDeserializer extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken != JsonToken.VALUE_STRING) {
      throw ctxt.mappingException(classOf[StringBuilder])
    }

    new StringBuilder(jp.getText)
  }
}
