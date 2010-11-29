package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import collection.mutable.Builder
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class MapDeserializer(builder: Builder[(Object, Object), Object],
                           valueType: JavaType,
                           valueDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
      throw ctxt.mappingException(valueType.getRawClass)
    }

    while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
      val name = jp.getCurrentName
      jp.nextToken()
      builder += ((name, valueDeserializer.deserialize(jp, ctxt)))
      jp.nextToken()
    }

    builder.result
  }
}
