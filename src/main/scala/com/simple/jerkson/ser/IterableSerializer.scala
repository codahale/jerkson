package com.codahale.jerkson.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

class IterableSerializer extends JsonSerializer[Iterable[_]] {
  def serialize(value: Iterable[_], json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartArray()
    for (element <- value) {
      provider.defaultSerializeValue(element, json)
    }
    json.writeEndArray()
  }
}
