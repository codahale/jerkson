package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class MapSerializer extends JsonSerializer[collection.Map[_ ,_]] {
  def serialize(map: collection.Map[_,_], json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for ((key, value) <- map) {
      provider.defaultSerializeField(key.toString, value, json)
    }
    json.writeEndObject()
  }
}
