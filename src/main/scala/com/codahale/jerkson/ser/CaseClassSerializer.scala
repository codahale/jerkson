package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.annotate.JsonIgnore

class CaseClassSerializer[A <: Product](klass: Class[_]) extends JsonSerializer[A] {
  private val nonIgnoredFields = klass.getDeclaredFields.filterNot { f =>
    f.getAnnotation(classOf[JsonIgnore]) != null || f.getName.contains("$")
  }

  private val methods = klass.getDeclaredMethods
                                .filter { _.getParameterTypes.isEmpty }
                                .map { m => m.getName -> m }.toMap
  
  def serialize(value: A, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for (field <- nonIgnoredFields) {
      val methodOpt = methods.get(field.getName)
      val fieldValue: Object = methodOpt.map { _.invoke(value) }.getOrElse(field.get(value))
      if (fieldValue != None) {
        json.writeFieldName(methodOpt.map {_.getName}.getOrElse(field.getName))
        if (fieldValue == null) {
          provider.getNullValueSerializer.serialize(null, json, provider)
        } else {
          val serializer = provider.findValueSerializer(fieldValue.getClass, null)
          serializer.serialize(fieldValue, json, provider)
        }
      }
    }
    json.writeEndObject()
  }
}
