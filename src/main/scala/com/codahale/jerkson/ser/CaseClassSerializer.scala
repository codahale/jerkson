package com.codahale.jerkson.ser

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.map.{SerializerProvider, JsonSerializer}
import org.codehaus.jackson.annotate.{JsonIgnore, JsonIgnoreProperties}
import org.codehaus.jackson.map.annotate.JsonCachable
import java.lang.reflect.Modifier
import com.codahale.jerkson.util.CaseClassSigParser
import annotation.tailrec

@JsonCachable
class CaseClassSerializer[A <: Product](klass: Class[_]) extends JsonSerializer[A] {
  private val paramNames = CaseClassSigParser.parseNames(klass)
  private val ignoredFields = if (klass.isAnnotationPresent(classOf[JsonIgnoreProperties])) {
    klass.getAnnotation(classOf[JsonIgnoreProperties]).value().toSet
  } else Set.empty[String]
  
  def serialize(obj: A, json: JsonGenerator, provider: SerializerProvider) {
    @tailrec
    def writeFields(idx: Int, fields: Seq[String]) {
      if (!fields.isEmpty) {
        val field = fields.head
        if (!ignoredFields(field)) {
          val value = obj.productElement(idx)
          if (value != None) {
            provider.defaultSerializeField(field, value, json)
          }
        }
        writeFields(idx + 1, fields.tail)
      }
    }

    json.writeStartObject()
    writeFields(0, paramNames)
    json.writeEndObject()
  }
}
