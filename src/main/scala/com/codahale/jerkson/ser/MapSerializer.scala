package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}

class MapSerializer extends JsonSerializer[Map[_,_]] {
  def serialize(value: Map[_, _], json: JsonGenerator, provider: SerializerProvider) = {
    json.writeStartObject()
    for ((key, value) <- value) {
      if (key == null) {
        provider.getNullKeySerializer.serialize(null, json, provider)
      } else {
        provider.getKeySerializer.serialize(key.asInstanceOf[Object], json, provider)
      }

      if (value == null) {
        provider.getNullValueSerializer.serialize(null, json, provider)
      } else {
        val obj = value.asInstanceOf[Object]
        val serializer = provider.findValueSerializer(obj.getClass)
        serializer.serialize(obj, json, provider)
      }
      json.flush()
    }
    json.writeEndObject()
  }
}
