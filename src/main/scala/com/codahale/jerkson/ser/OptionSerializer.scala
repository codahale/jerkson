package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class OptionSerializer extends JsonSerializer[Option[_]] {
  def serialize(value: Option[_], json: JsonGenerator,
                provider: SerializerProvider) {
    provider.defaultSerializeValue(value.orNull, json)
  }
}
