package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}

/**
 *
 * @author coda
 */
class SeqSerializer extends JsonSerializer[Object] {
  def serialize(value: Object, json: JsonGenerator, provider: SerializerProvider) = {
    json.writeStartArray()
    for (element <- value.asInstanceOf[Seq[_]]) {
      if (element == null) {
        provider.getNullValueSerializer.serialize(null, json, provider)
      } else {
        val obj = element.asInstanceOf[Object]
        val serializer = provider.findValueSerializer(obj.getClass)
        serializer.serialize(obj, json, provider)
      }
    }
    json.writeEndArray()
  }
}
