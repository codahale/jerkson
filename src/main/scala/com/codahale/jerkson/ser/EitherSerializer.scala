package com.codahale.jerkson.ser

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

class EitherSerializer extends JsonSerializer[Either[_, _]] {
  def serialize(value: Either[_, _], json: JsonGenerator, provider: SerializerProvider) {
    provider.defaultSerializeValue(value match {
      case Left(o) => o.asInstanceOf[Object]
      case Right(o) => o.asInstanceOf[Object]
    }, json)
  }
}
