package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.annotate.{JsonIgnore, JsonIgnoreProperties}
import org.codehaus.jackson.map.annotate.JsonCachable
import java.lang.reflect.Modifier

@JsonCachable
class CaseClassSerializer[A <: Product](klass: Class[_]) extends JsonSerializer[A] {
  private val ignoredFields = if (klass.isAnnotationPresent(classOf[JsonIgnoreProperties])) {
    klass.getAnnotation(classOf[JsonIgnoreProperties]).value().toSet
  } else Set.empty[String]
  
  private val nonIgnoredFields = klass.getDeclaredFields.filterNot { f =>
    f.getAnnotation(classOf[JsonIgnore]) != null ||
    ignoredFields.contains(f.getName) ||
      (f.getModifiers & Modifier.TRANSIENT) != 0 ||
      f.getName.contains("$")
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
        val fieldName = methodOpt.map { _.getName }.getOrElse(field.getName)
        provider.defaultSerializeField(fieldName, fieldValue, json)
      }
    }
    json.writeEndObject()
  }
}
