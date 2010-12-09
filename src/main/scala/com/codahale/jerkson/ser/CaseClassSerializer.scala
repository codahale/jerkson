package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import java.lang.reflect.{Method, Field}
import org.codehaus.jackson.annotate.JsonIgnore

class CaseClassSerializer extends JsonSerializer[Product] {
  def serialize(value: Product, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()

    val nonIgnoredFields = value.getClass.getDeclaredFields.filterNot { f =>
      f.getAnnotation(classOf[JsonIgnore]) != null || f.getName.contains("$")
    }

    val methods = value.getClass.getDeclaredMethods
                        .filter { _.getParameterTypes.isEmpty }
                        .map {m => m.getName -> m}.toMap
    
    for (field <- nonIgnoredFields) {
      methods.get(field.getName) match {
        case Some(method) => serializeMethod(method, json, value, provider)
        case None => serializeField(field, json, value, provider)
      }
    }
    json.writeEndObject()
  }

  private def serializeMethod(method: Method, json: JsonGenerator, value: Product, provider: SerializerProvider): Unit = {
    method.setAccessible(true)
    json.writeFieldName(method.getName)
    val methodValue: Object = method.invoke(value)
    if (methodValue == null) {
      provider.getNullValueSerializer.serialize(null, json, provider)
    } else {
      val serializer = provider.findValueSerializer(methodValue.getClass)
      serializer.serialize(methodValue, json, provider)
    }
  }

  private def serializeField(field: Field, json: JsonGenerator, value: Product, provider: SerializerProvider): Unit = {
    field.setAccessible(true)
    json.writeFieldName(field.getName)
    val fieldValue: Object = field.get(value)
    if (fieldValue == null) {
      provider.getNullValueSerializer.serialize(null, json, provider)
    } else {
      val serializer = provider.findValueSerializer(fieldValue.getClass)
      serializer.serialize(fieldValue, json, provider)
    }
  }
}
