package com.codahale.jerkson.ser

import java.lang.reflect.Modifier
import com.codahale.jerkson.JsonSnakeCase
import com.codahale.jerkson.Util._
import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.annotate.{JsonIgnore, JsonIgnoreProperties}
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class CaseClassSerializer[A <: Product](klass: Class[_]) extends JsonSerializer[A] {
  private val isSnakeCase = klass.isAnnotationPresent(classOf[JsonSnakeCase])
  private val ignoredFields = if (klass.isAnnotationPresent(classOf[JsonIgnoreProperties])) {
    klass.getAnnotation(classOf[JsonIgnoreProperties]).value().toSet
  } else Set.empty[String]

  private val nonIgnoredFields = klass.getDeclaredFields.filterNot { f =>
    f.getAnnotation(classOf[JsonIgnore]) != null ||
    ignoredFields(f.getName) ||
      (f.getModifiers & Modifier.TRANSIENT) != 0 ||
      f.getName.contains("$")
  }

  private val methods = klass.getDeclaredMethods
                                .filter { _.getParameterTypes.isEmpty }
                                .map { m => m.getName -> m }.toMap

  def serialize(value: A, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for (field <- nonIgnoredFields) {
      val fieldName = field.getName
      val methodOpt = methods.get(fieldName)
      val fieldValue: Object = methodOpt.map { _.invoke(value) }.getOrElse(field.get(value))
      if (fieldValue != None) {
        provider.defaultSerializeField(if (isSnakeCase) snakeCase(fieldName) else fieldName, fieldValue, json)
      }
    }
    json.writeEndObject()
  }
}
