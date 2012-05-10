package com.codahale.jerkson.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

class StringBuilderSerializer extends JsonSerializer[StringBuilder] {
  def serialize(value: StringBuilder, json: JsonGenerator, provider: SerializerProvider) {
    json.writeString(value.toString())
  }
}
