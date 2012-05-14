package com.codahale.jerkson.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

class OptionSerializer extends JsonSerializer[Option[_]] {
  def serialize(value: Option[_], json: JsonGenerator,
                provider: SerializerProvider) {
    provider.defaultSerializeValue(value.orNull, json)
  }
}
