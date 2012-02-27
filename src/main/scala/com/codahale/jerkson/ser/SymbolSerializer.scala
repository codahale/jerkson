package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class SymbolSerializer extends JsonSerializer[Symbol] {
  def serialize(value: Symbol, json: JsonGenerator, provider: SerializerProvider) {
    json.writeString(value.name)
  }
}
