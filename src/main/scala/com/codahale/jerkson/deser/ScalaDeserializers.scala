package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map._
import collection.MapLike
import com.codahale.jerkson.AST.JValue
import scala.collection.{immutable, mutable}
import scala.collection.generic.{MapFactory, GenericCompanion}

class ScalaDeserializers extends Deserializers.None {
  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig,
                            provider: DeserializerProvider, beanDesc: BeanDescription,
                            property: BeanProperty) = {
    val klass = javaType.getRawClass
    if (klass == classOf[List[_]] || klass == classOf[immutable.List[_]]) {
      createSeqDeserializer(config, javaType, List, provider, property)
    } else if (klass == classOf[Seq[_]] || klass == classOf[immutable.Seq[_]] || klass == classOf[Iterable[_]]) {
      createSeqDeserializer(config, javaType, Seq, provider, property)
    } else if (klass == classOf[Stream[_]] || klass == classOf[immutable.Stream[_]]) {
      createSeqDeserializer(config, javaType, Stream, provider, property)
    } else if (klass == classOf[Vector[_]]) {
      createSeqDeserializer(config, javaType, Vector, provider, property)
    } else if (klass == classOf[IndexedSeq[_]] || klass == classOf[immutable.IndexedSeq[_]]) {
      createSeqDeserializer(config, javaType, IndexedSeq, provider, property)
    } else if (klass == classOf[mutable.ResizableArray[_]]) {
      createSeqDeserializer(config, javaType, mutable.ResizableArray, provider, property)
    } else if (klass == classOf[mutable.ArraySeq[_]]) {
      createSeqDeserializer(config, javaType, mutable.ArraySeq, provider, property)
    } else if (klass == classOf[mutable.MutableList[_]]) {
      createSeqDeserializer(config, javaType, mutable.MutableList, provider, property)
    } else if (klass == classOf[mutable.Queue[_]]) {
      createSeqDeserializer(config, javaType, mutable.Queue, provider, property)
    } else if (klass == classOf[mutable.ListBuffer[_]]) {
      createSeqDeserializer(config, javaType, mutable.ListBuffer, provider, property)
    } else if (klass == classOf[mutable.ArrayBuffer[_]]) {
      createSeqDeserializer(config, javaType, mutable.ArrayBuffer, provider, property)
    } else if (klass == classOf[immutable.HashSet[_]]) {
      createSeqDeserializer(config, javaType, immutable.HashSet, provider, property)
    } else if (klass == classOf[collection.BitSet] || klass == classOf[immutable.BitSet]) {
      new BitSetDeserializer(immutable.BitSet)
    } else if (klass == classOf[mutable.BitSet]) {
      new BitSetDeserializer(mutable.BitSet)
    } else if (klass == classOf[Set[_]]) {
      createSeqDeserializer(config, javaType, Set, provider, property)
    } else if (klass == classOf[mutable.HashSet[_]]) {
      createSeqDeserializer(config, javaType, mutable.HashSet, provider, property)
    } else if (klass == classOf[mutable.LinkedHashSet[_]]) {
      createSeqDeserializer(config, javaType, mutable.LinkedHashSet, provider, property)
    } else if (klass == classOf[Iterator[_]]) {
      val elementType = javaType.containedType(0)
      new IteratorDeserializer(elementType, provider.findTypedValueDeserializer(config, elementType, property))
    } else if (klass == classOf[immutable.HashMap[_, _]] || klass == classOf[Map[_, _]]) {
      createImmutableMapDeserializer(config, javaType, immutable.HashMap, provider, property)
    } else if (klass == classOf[mutable.HashMap[_, _]] || klass == classOf[mutable.Map[_, _]]) {
      if (javaType.containedType(0).getRawClass == classOf[String]) {
        val valueType = javaType.containedType(1)
        new MutableMapDeserializer(valueType, provider.findTypedValueDeserializer(config, valueType, property))
      } else {
        null
      }
    } else if (klass == classOf[mutable.LinkedHashMap[_, _]]) {
      if (javaType.containedType(0).getRawClass == classOf[String]) {
        val valueType = javaType.containedType(1)
        new MutableLinkedHashMapDeserializer(valueType, provider.findTypedValueDeserializer(config, valueType, property))
      } else {
        null
      }
    } else if (klass == classOf[Option[_]]) {
      createOptionDeserializer(config, javaType, provider, property)
    } else if (klass == classOf[JValue]) {
      new JValueDeserializer
    } else if (klass == classOf[BigInt]) {
      new BigIntDeserializer
    } else if (klass == classOf[BigDecimal]) {
      new BigDecimalDeserializer
    } else if (klass == classOf[Either[_,_]]) {
      new EitherDeserializer(config, javaType, provider)
    } else if (classOf[Product].isAssignableFrom(klass)) {
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

  private def createImmutableMapDeserializer[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](config: DeserializationConfig,
                                                                                        javaType: JavaType,
                                                                                        companion: MapFactory[CC],
                                                                                        provider: DeserializerProvider,
                                                                                        property: BeanProperty) = {
    if (javaType.containedType(0).getRawClass == classOf[String]) {
      val valueType = javaType.containedType(1)
      new ImmutableMapDeserializer[CC](companion, valueType, provider.findTypedValueDeserializer(config, valueType, property))
    } else {
      null
    }
  }
}
