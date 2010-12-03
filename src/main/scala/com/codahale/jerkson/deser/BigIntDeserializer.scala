package com.codahale.jerkson.deser

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}

@JsonCachable
class BigIntDeserializer extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == null) {
      jp.nextToken()
    }

    if (jp.getCurrentToken != JsonToken.VALUE_NUMBER_INT) {
      throw ctxt.mappingException(classOf[BigInt])
    }

    BigInt(jp.getText)
  }
}
