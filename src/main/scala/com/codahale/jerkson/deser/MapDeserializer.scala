package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import org.codehaus.jackson.map.annotate.JsonCachable
import collection.generic.{MapFactory, GenericCompanion}
import collection.MapLike

@JsonCachable
class MapDeserializer[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](companion: MapFactory[CC],
                                                                          valueType: JavaType,
                                                                          valueDeserializer: JsonDeserializer[Object])
  extends JsonDeserializer[Object] {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext):Object = {
    val builder = companion.newBuilder[String, Object]

    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken == JsonToken.END_OBJECT) {
      return builder.result
    }

    if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
      throw ctxt.mappingException(valueType.getRawClass)
    }

    while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
      val name = jp.getCurrentName
      jp.nextToken()
      builder += ((name, valueDeserializer.deserialize(jp, ctxt)))
      jp.nextToken()
    }

    builder.result
  }
}
