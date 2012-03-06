package com.codahale.jerkson.deser

import org.codehaus.jackson.map.{DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.{JsonToken, JsonParser}
import com.codahale.jerkson.AST._
import collection.mutable.ArrayBuffer
import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.`type`.TypeFactory
import com.codahale.jerkson.Types

@JsonCachable
class JValueDeserializer(factory: TypeFactory, klass: Class[_]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext): Object = {
    if (jp.getCurrentToken == null) {
      jp.nextToken()
    }

    val value = jp.getCurrentToken match {
      case JsonToken.VALUE_NULL => JNull
      case JsonToken.VALUE_NUMBER_INT => JInt(BigInt(jp.getText))
      case JsonToken.VALUE_NUMBER_FLOAT => JFloat(jp.getDoubleValue)
      case JsonToken.VALUE_STRING => JString(jp.getText)
      case JsonToken.VALUE_TRUE => JBoolean(true)
      case JsonToken.VALUE_FALSE => JBoolean(false)
      case JsonToken.START_ARRAY => {
        JArray(jp.getCodec.readValue(jp, Types.build(factory, manifest[List[JValue]])))
      }
      case JsonToken.START_OBJECT => {
        jp.nextToken()
        deserialize(jp, ctxt)
      }
      case JsonToken.FIELD_NAME | JsonToken.END_OBJECT => {
        val fields = new ArrayBuffer[JField]
        while (jp.getCurrentToken != JsonToken.END_OBJECT) {
          val name = jp.getCurrentName
          jp.nextToken()
          fields += JField(name, jp.getCodec.readValue(jp, Types.build(factory, manifest[JValue])))
          jp.nextToken()
        }
        JObject(fields.toList)
      }
      case _ => throw ctxt.mappingException(classOf[JValue])
    }

    if (!klass.isAssignableFrom(value.getClass)) {
      throw ctxt.mappingException(klass)
    }

    value
  }
}
