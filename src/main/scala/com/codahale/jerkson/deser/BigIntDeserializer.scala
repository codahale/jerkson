package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.core.JsonParser

class BigIntDeserializer extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == null) {
      jp.nextToken()
    }

    try {
      BigInt(jp.getText)
    } catch {
      case e: NumberFormatException =>
        throw ctxt.mappingException(classOf[BigInt])
    }
  }

  override def isCachable = true
}
