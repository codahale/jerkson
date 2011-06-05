package com.codahale.jerkson.deser

import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.`type`.JavaType
import scala.collection.immutable.LongMap

@JsonCachable
class LongMapDeserializer(valueType: JavaType,
                          valueDeserializer: JsonDeserializer[Object]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    var map = LongMap.empty[Object]

    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    while (jp.getCurrentToken != JsonToken.END_OBJECT) {
      try {
        val name = jp.getCurrentName.toLong
        jp.nextToken()
        map += ((name, valueDeserializer.deserialize(jp, ctxt)))
        jp.nextToken()
      } catch {
        case e: IllegalArgumentException => throw ctxt.mappingException(classOf[LongMap[_]])
      }
    }
    map
  }
}
