package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}

class MapSerializer extends JsonSerializer[Object] {
  def serialize(value: Object, json: JsonGenerator, provider: SerializerProvider) = {
    json.writeStartObject()
    for ((key, value) <- value.asInstanceOf[Map[_,_]]) {
      if (key == null) {
        provider.getNullKeySerializer.serialize(null, json, provider)
      } else {
        json.writeFieldName(key.asInstanceOf[String])
      }

      if (value == null) {
        provider.getNullValueSerializer.serialize(null, json, provider)
      } else {
        val obj = value.asInstanceOf[Object]
        val serializer = provider.findValueSerializer(obj.getClass)
        serializer.serialize(obj, json, provider)
      }
    }
    json.writeEndObject()
  }
}
