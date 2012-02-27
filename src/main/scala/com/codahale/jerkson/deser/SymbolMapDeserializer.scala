package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.map.annotate.JsonCachable

/** Deserializes a Map indexed by a Scala Symbol */
@JsonCachable
class SymbolMapDeserializer (valueType: JavaType,
                             valueDeserializer: JsonDeserializer[Object]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    var map = Map.empty[Symbol, Object]

    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken != JsonToken.FIELD_NAME &&
      jp.getCurrentToken != JsonToken.END_OBJECT) {
      throw ctxt.mappingException(valueType.getRawClass)
    }

    while (jp.getCurrentToken != JsonToken.END_OBJECT) {
      try {
        val name = Symbol(jp.getCurrentName)
        jp.nextToken()
        map += ((name, valueDeserializer.deserialize(jp, ctxt)))
        jp.nextToken()
      } catch {
        case e: IllegalArgumentException => throw ctxt.mappingException(classOf[Symbol])
      }
    }

    map
  }
}
