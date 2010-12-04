package com.codahale.jerkson.deser

import org.codehaus.jackson.map.deser.BeanDeserializerFactory
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializerProvider, DeserializationConfig}
import com.codahale.jerkson.AST.JValue
import collection.MapLike
import collection.generic.{MapFactory, GenericCompanion}

class ScalaDeserializerFactory extends BeanDeserializerFactory {
  override def createBeanDeserializer(config: DeserializationConfig, javaType: JavaType, provider: DeserializerProvider) = {
    if (javaType.getRawClass == classOf[List[_]]) {
      createSeqDeserializer(config, javaType, List, provider)
    } else if (javaType.getRawClass == classOf[Seq[_]]) {
      createSeqDeserializer(config, javaType, Seq, provider)
    } else if (javaType.getRawClass == classOf[Vector[_]]) {
      createSeqDeserializer(config, javaType, Vector, provider)
    } else if (javaType.getRawClass == classOf[IndexedSeq[_]]) {
      createSeqDeserializer(config, javaType, IndexedSeq, provider)
    } else if (javaType.getRawClass == classOf[Map[_, _]]) {
      createMapDeserializer(config, javaType, Map, provider)
    } else if (javaType.getRawClass == classOf[Option[_]]) {
      createOptionDeserializer(config, javaType, provider)
    } else if (javaType.getRawClass == classOf[JValue]) {
      new JValueDeserializer
    } else if (javaType.getRawClass == classOf[BigInt]) {
      new BigIntDeserializer
    } else if (javaType.getRawClass == classOf[BigDecimal]) {
      new BigDecimalDeserializer
    } else if (classOf[Product].isAssignableFrom(javaType.getRawClass)) {
      new CaseClassDeserializer(config, javaType, provider)
    } else super.createBeanDeserializer(config, javaType, provider)
  }

  private def createSeqDeserializer[CC[X] <: Traversable[X]](config: DeserializationConfig,
                                                             javaType: JavaType,
                                                             companion: GenericCompanion[CC],
                                                             provider: DeserializerProvider) = {
    val elementType = javaType.containedType(0)
    new SeqDeserializer[CC](companion, elementType, provider.findTypedValueDeserializer(config, elementType))
  }

  private def createOptionDeserializer(config: DeserializationConfig,
                                       javaType: JavaType,
                                       provider: DeserializerProvider) = {
    val elementType = javaType.containedType(0)
    new OptionDeserializer(elementType, provider.findTypedValueDeserializer(config, elementType))
  }

  private def createMapDeserializer[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](config: DeserializationConfig,
                                                                                        javaType: JavaType,
                                                                                        companion: MapFactory[CC],
                                                                                        provider: DeserializerProvider) = {
    if (javaType.containedType(0).getRawClass == classOf[String]) {
      val valueType = javaType.containedType(1)
      new MapDeserializer[CC](companion, valueType, provider.findTypedValueDeserializer(config, valueType))
    } else {
      null
    }
  }
}
