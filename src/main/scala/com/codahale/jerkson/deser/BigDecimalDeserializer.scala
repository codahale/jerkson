package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.core.JsonParser

class BigDecimalDeserializer extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == null) {
      jp.nextToken()
    }

    try {
      BigDecimal(jp.getText)
    } catch {
      case e: NumberFormatException =>
        throw ctxt.mappingException(classOf[BigDecimal])
    }
  }

  override def isCachable = true
}
