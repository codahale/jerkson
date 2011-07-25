package com.codahale.jerkson.deser

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.JsonParser

@JsonCachable
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
}
