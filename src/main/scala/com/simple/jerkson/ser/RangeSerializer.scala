package com.codahale.jerkson.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

class RangeSerializer extends JsonSerializer[Range] {
  def serialize(value: Range, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    json.writeNumberField("start", value.start)
    json.writeNumberField("end", value.end)

    if (value.step != 1) {
      json.writeNumberField("step", value.step)
    }

    if (value.isInclusive) {
      json.writeBooleanField("inclusive", value.isInclusive)
    }

    json.writeEndObject()
  }
}
