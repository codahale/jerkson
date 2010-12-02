package com.codahale.jerkson.deser

import org.codehaus.jackson.map.deser.BeanDeserializerFactory
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializerProvider, DeserializationConfig}
import collection.mutable.Builder
import com.codahale.jerkson.AST.JValue

class ScalaDeserializerFactory extends BeanDeserializerFactory {
  override def createBeanDeserializer(config: DeserializationConfig, javaType: JavaType, provider: DeserializerProvider) = {
    if (javaType.getRawClass == classOf[List[_]]) {
      createSeqDeserializer(config, javaType, List.newBuilder, provider)
    } else if (javaType.getRawClass == classOf[Seq[_]]) {
      createSeqDeserializer(config, javaType, Seq.newBuilder, provider)
    } else if (javaType.getRawClass == classOf[Vector[_]]) {
      createSeqDeserializer(config, javaType, Vector.newBuilder, provider)
    } else if (javaType.getRawClass == classOf[IndexedSeq[_]]) {
      createSeqDeserializer(config, javaType, IndexedSeq.newBuilder, provider)
    } else if (javaType.getRawClass == classOf[Map[_, _]]) {
      createMapDeserializer(config, javaType, Map.newBuilder, provider)
    } else if (javaType.getRawClass == classOf[Option[_]]) {
      createOptionDeserializer(config, javaType, provider)
    } else if (javaType.getRawClass == classOf[JValue]) {
      new JValueDeserializer
    } else if (classOf[Product].isAssignableFrom(javaType.getRawClass)) {
      new CaseClassDeserializer(config, javaType, provider)
    } else super.createBeanDeserializer(config, javaType, provider)
  }

  private def createSeqDeserializer(config: DeserializationConfig, javaType: JavaType, newBuilder: => Builder[Object, Object], provider: DeserializerProvider) = {
    val elementType = javaType.containedType(0)
    new SeqDeserializer(newBuilder, elementType, provider.findTypedValueDeserializer(config, elementType))
  }

  private def createOptionDeserializer(config: DeserializationConfig, javaType: JavaType, provider: DeserializerProvider) = {
    val elementType = javaType.containedType(0)
    new OptionDeserializer(elementType, provider.findTypedValueDeserializer(config, elementType))
  }

  private def createMapDeserializer(config: DeserializationConfig, javaType: JavaType, newBuilder: => Builder[(Object, Object), Object], provider: DeserializerProvider) = {
    if (javaType.containedType(0).getRawClass == classOf[String]) {
      val valueType = javaType.containedType(1)
      new MapDeserializer(newBuilder, valueType, provider.findTypedValueDeserializer(config, valueType))
    } else {
      null
    }
  }
}
