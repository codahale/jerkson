package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import java.lang.reflect.Field
import org.codehaus.jackson.annotate.{JsonIgnoreProperties, JsonIgnore}

object CaseClassSerializer {
  val PRODUCT = classOf[Product]
  val OBJECT  = classOf[AnyRef]
  val OPTION  = classOf[Option[_]]

  def classIgnoredProperties(someOtherClass: Class[_]): Set[String] = {
    Option(someOtherClass.getAnnotation(classOf[JsonIgnoreProperties])).map(_.value).flatten.toSet
  }
}

class CaseClassSerializer[A <: Product](klass: Class[_]) extends JsonSerializer[A] {
  import CaseClassSerializer._
  private[this] val thisClassIgnoredProperties = classIgnoredProperties(klass)

  private val nonIgnoredFields = {
    def collectFields(clazz: Class[_], gotSoFar: Vector[Field]): Vector[Field] = {
      clazz match {
        case null | PRODUCT | OBJECT => // TODO others?
          gotSoFar

        case someOtherClass =>
          val classIgnore = thisClassIgnoredProperties ++ classIgnoredProperties(someOtherClass)

          val fields = someOtherClass.getDeclaredFields.filterNot { f =>
            val fieldName = f.getName
            classIgnore.contains(fieldName) || f.getAnnotation(classOf[JsonIgnore]) != null || fieldName.contains("$")
          }

          collectFields(someOtherClass.getSuperclass, Vector(fields: _*) ++ gotSoFar)
      }
    }

    collectFields(klass, Vector()).toArray
  }

  private val methods = klass.getMethods
                                .filter { _.getParameterTypes.isEmpty }
                                .map { m => m.getName -> m }.toMap
  
  def serialize(value: A, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartObject()
    for (field <- nonIgnoredFields) {
      val methodOpt = methods.get(field.getName)
      json.writeFieldName(methodOpt.map { _.getName }.getOrElse(field.getName))
      val fieldValue: Object = methodOpt.map { _.invoke(value) }.getOrElse(field.get(value))
      if (fieldValue == null) {
        provider.getNullValueSerializer.serialize(null, json, provider)
      } else {
        val serializer = provider.findValueSerializer(fieldValue.getClass)
        serializer.serialize(fieldValue, json, provider)
      }
      json.flush()
    }
    json.writeEndObject()
  }
}
