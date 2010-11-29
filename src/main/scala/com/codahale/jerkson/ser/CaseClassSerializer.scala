package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}

class CaseClassSerializer extends JsonSerializer[Object] {
  def serialize(value: Object, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for (field <- value.getClass.getDeclaredFields) {
      field.setAccessible(true)
      json.writeFieldName(field.getName)
      val fieldValue = field.get(value)
      println(fieldValue)
      if (fieldValue == null) {
        provider.getNullValueSerializer.serialize(null, json, provider)
      } else {
        val serializer = provider.findValueSerializer(fieldValue.getClass)
        serializer.serialize(fieldValue, json, provider)
      }
    }
    json.writeEndObject()
  }
}
