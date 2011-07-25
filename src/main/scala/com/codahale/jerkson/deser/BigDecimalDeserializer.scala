package com.codahale.jerkson.deser

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.JsonParser

@JsonCachable
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
}
