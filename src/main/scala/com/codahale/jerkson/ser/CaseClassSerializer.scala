package com.codahale.jerkson.ser

import java.lang.reflect.Modifier
import com.codahale.jerkson.JsonSnakeCase
import com.codahale.jerkson.Util._
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties, JsonProperty}
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

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
                                
  private val jsonGetters = methods
							  	.filter { _._2.getAnnotation(classOf[JsonProperty]) != null }
							    .map { m => m._2.getAnnotation(classOf[JsonProperty]).value -> m._2 }.toMap                           
  
  def serialize(value: A, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for (field <- nonIgnoredFields) {
      val methodOpt = methods.get(field.getName)
      val getterOpt = jsonGetters.get(field.getName)
      val fieldValue: Object = getterOpt.map { _.invoke(value) }.getOrElse(methodOpt.map { _.invoke(value) }.getOrElse(field.get(value)))
      if (fieldValue != None) {
        val fieldName = methodOpt.map { _.getName }.getOrElse(field.getName)
        provider.defaultSerializeField(if (isSnakeCase) snakeCase(fieldName) else fieldName, fieldValue, json)
      }
    }
    json.writeEndObject()
  }
}
