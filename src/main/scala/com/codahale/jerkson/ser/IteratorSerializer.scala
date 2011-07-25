package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class IteratorSerializer extends JsonSerializer[Iterator[_]] {
  def serialize(value: Iterator[_], json: JsonGenerator,
                provider: SerializerProvider) {
    json.writeStartArray()
    for (element <- value) {
      provider.defaultSerializeValue(element, json)
    }
    json.writeEndArray()
  }
}
