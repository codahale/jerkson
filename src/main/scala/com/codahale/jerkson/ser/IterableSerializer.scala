package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class IterableSerializer extends JsonSerializer[Iterable[_]] {
  def serialize(value: Iterable[_], json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartArray()
    for (element <- value) {
      if (element == null) {
        provider.getNullValueSerializer.serialize(null, json, provider)
      } else {
        val obj = element.asInstanceOf[Object]
        val serializer = provider.findValueSerializer(obj.getClass, null)
        serializer.serialize(obj, json, provider)
      }
    }
    json.writeEndArray()
  }
}
