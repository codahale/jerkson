package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map._
import collection.generic.{MapFactory, GenericCompanion}
import collection.MapLike
import com.codahale.jerkson.AST.JValue

/**
 *
 * @author coda
 */
class ScalaDeserializers extends Deserializers.None {
  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig,
                            provider: DeserializerProvider, beanDesc: BeanDescription,
                            property: BeanProperty) = {
    if (javaType.getRawClass == classOf[List[_]]) {
      createSeqDeserializer(config, javaType, List, provider, property)
    } else if (javaType.getRawClass == classOf[Seq[_]]) {
      createSeqDeserializer(config, javaType, Seq, provider, property)
    } else if (javaType.getRawClass == classOf[Vector[_]]) {
      createSeqDeserializer(config, javaType, Vector, provider, property)
    } else if (javaType.getRawClass == classOf[IndexedSeq[_]]) {
      createSeqDeserializer(config, javaType, IndexedSeq, provider, property)
    } else if (javaType.getRawClass == classOf[Set[_]]) {
      createSeqDeserializer(config, javaType, Set, provider, property)
    } else if (javaType.getRawClass == classOf[Map[_, _]]) {
      createMapDeserializer(config, javaType, Map, provider, property)
    } else if (javaType.getRawClass == classOf[Option[_]]) {
      createOptionDeserializer(config, javaType, provider, property)
    } else if (javaType.getRawClass == classOf[JValue]) {
      new JValueDeserializer
    } else if (javaType.getRawClass == classOf[BigInt]) {
      new BigIntDeserializer
    } else if (javaType.getRawClass == classOf[BigDecimal]) {
      new BigDecimalDeserializer
    } else if (javaType.getRawClass == classOf[Either[_,_]]) {
      new EitherDeserializer(config, javaType, provider)
    } else if (classOf[Product].isAssignableFrom(javaType.getRawClass)) {
      new CaseClassDeserializer(config, javaType, provider)
    } else null
  }

  private def createSeqDeserializer[CC[X] <: Traversable[X]](config: DeserializationConfig,
                                                             javaType: JavaType,
                                                             companion: GenericCompanion[CC],
                                                             provider: DeserializerProvider,
                                                             property: BeanProperty) = {
    val elementType = javaType.containedType(0)
    new SeqDeserializer[CC](companion, elementType, provider.findTypedValueDeserializer(config, elementType, property))
  }

  private def createOptionDeserializer(config: DeserializationConfig,
                                       javaType: JavaType,
                                       provider: DeserializerProvider,
                                       property: BeanProperty) = {
    val elementType = javaType.containedType(0)
    new OptionDeserializer(elementType, provider.findTypedValueDeserializer(config, elementType, property))
  }

  private def createMapDeserializer[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](config: DeserializationConfig,
                                                                                        javaType: JavaType,
                                                                                        companion: MapFactory[CC],
                                                                                        provider: DeserializerProvider,
                                                                                        property: BeanProperty) = {
    if (javaType.containedType(0).getRawClass == classOf[String]) {
      val valueType = javaType.containedType(1)
      new MapDeserializer[CC](companion, valueType, provider.findTypedValueDeserializer(config, valueType, property))
    } else {
      null
    }
  }
}
