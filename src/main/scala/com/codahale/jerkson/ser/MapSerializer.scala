package com.codahale.jerkson.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

class MapSerializer[K,V] extends JsonSerializer[collection.Map[K ,V]] {
  def serialize(map: collection.Map[K,V], json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for ((key, value) <- map) {
      provider.defaultSerializeField(key.toString, value, json)
    }
    json.writeEndObject()
  }
}
