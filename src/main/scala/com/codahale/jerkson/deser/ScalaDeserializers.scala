package com.codahale.jerkson.deser

import com.fasterxml.jackson.databind._
import scala.collection.{Traversable, MapLike, immutable, mutable}
import com.codahale.jerkson.AST.{JNull, JValue}
import scala.collection.generic.{MapFactory, GenericCompanion}
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.Module.SetupContext

class ScalaDeserializers(classLoader: ClassLoader, context: SetupContext) extends Deserializers.Base {
  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig,
                            beanDesc: BeanDescription): JsonDeserializer[_] = {
    val klass = javaType.getRawClass
    if (klass == classOf[Range] || klass == classOf[immutable.Range]) {
      new RangeDeserializer
    } else if (klass == classOf[StringBuilder]) {
      new StringBuilderDeserializer
    } else if (klass == classOf[List[_]] || klass == classOf[immutable.List[_]]) {
      createSeqDeserializer(config, javaType, List)
    } else if (klass == classOf[Seq[_]] || klass == classOf[immutable.Seq[_]] ||
               klass == classOf[Iterable[_]] || klass == classOf[Traversable[_]] ||
               klass == classOf[immutable.Traversable[_]]) {
      createSeqDeserializer(config, javaType, Seq)
    } else if (klass == classOf[Stream[_]] || klass == classOf[immutable.Stream[_]]) {
      createSeqDeserializer(config, javaType, Stream)
    } else if (klass == classOf[immutable.Queue[_]]) {
      createSeqDeserializer(config, javaType, immutable.Queue)
    } else if (klass == classOf[Vector[_]]) {
      createSeqDeserializer(config, javaType, Vector)
    } else if (klass == classOf[IndexedSeq[_]] || klass == classOf[immutable.IndexedSeq[_]]) {
      createSeqDeserializer(config, javaType, IndexedSeq)
    } else if (klass == classOf[mutable.ResizableArray[_]]) {
      createSeqDeserializer(config, javaType, mutable.ResizableArray)
    } else if (klass == classOf[mutable.ArraySeq[_]]) {
      createSeqDeserializer(config, javaType, mutable.ArraySeq)
    } else if (klass == classOf[mutable.MutableList[_]]) {
      createSeqDeserializer(config, javaType, mutable.MutableList)
    } else if (klass == classOf[mutable.Queue[_]]) {
      createSeqDeserializer(config, javaType, mutable.Queue)
    } else if (klass == classOf[mutable.ListBuffer[_]]) {
      createSeqDeserializer(config, javaType, mutable.ListBuffer)
    } else if (klass == classOf[mutable.ArrayBuffer[_]] || klass == classOf[mutable.Traversable[_]]) {
      createSeqDeserializer(config, javaType, mutable.ArrayBuffer)
    } else if (klass == classOf[collection.BitSet] || klass == classOf[immutable.BitSet]) {
      new BitSetDeserializer(immutable.BitSet)
    } else if (klass == classOf[mutable.BitSet]) {
      new BitSetDeserializer(mutable.BitSet)
    } else if (klass == classOf[immutable.HashSet[_]]) {
      createSeqDeserializer(config, javaType, immutable.HashSet)
    } else if (klass == classOf[Set[_]] || klass == classOf[immutable.Set[_]] || klass == classOf[collection.Set[_]]) {
      createSeqDeserializer(config, javaType, Set)
    } else if (klass == classOf[mutable.HashSet[_]]) {
      createSeqDeserializer(config, javaType, mutable.HashSet)
    } else if (klass == classOf[mutable.LinkedHashSet[_]]) {
      createSeqDeserializer(config, javaType, mutable.LinkedHashSet)
    } else if (klass == classOf[Iterator[_]] || klass == classOf[BufferedIterator[_]]) {
      val elementType = javaType.containedType(0)
      new IteratorDeserializer(elementType)
    } else if (klass == classOf[immutable.HashMap[_, _]] || klass == classOf[Map[_, _]] || klass == classOf[collection.Map[_, _]]) {
      createImmutableMapDeserializer(config, javaType, immutable.HashMap)
    } else if (klass == classOf[immutable.IntMap[_]]) {
      val valueType = javaType.containedType(0)
      new IntMapDeserializer(valueType)
    } else if (klass == classOf[immutable.LongMap[_]]) {
      val valueType = javaType.containedType(0)
      new LongMapDeserializer(valueType)
    } else if (klass == classOf[mutable.HashMap[_, _]] || klass == classOf[mutable.Map[_, _]]) {
      if (javaType.containedType(0).getRawClass == classOf[String]) {
        val valueType = javaType.containedType(1)
        new MutableMapDeserializer(valueType)
      } else {
        null
      }
    } else if (klass == classOf[mutable.LinkedHashMap[_, _]]) {
      if (javaType.containedType(0).getRawClass == classOf[String]) {
        val valueType = javaType.containedType(1)
        new MutableLinkedHashMapDeserializer(valueType)
      } else {
        null
      }
    } else if (klass == classOf[Option[_]]) {
      createOptionDeserializer(config, javaType)
    } else if (classOf[JValue].isAssignableFrom(klass) || klass == JNull.getClass) {
      new JValueDeserializer(config.getTypeFactory, klass)
    } else if (klass == classOf[BigInt]) {
      new BigIntDeserializer
    } else if (klass == classOf[BigDecimal]) {
      new BigDecimalDeserializer
    } else if (klass == classOf[Either[_,_]]) {
      new EitherDeserializer(config, javaType)
    } else if (classOf[Product].isAssignableFrom(klass)) {
      new CaseClassDeserializer(config, javaType, classLoader)
    } else null
  }

  private def createSeqDeserializer[CC[X] <: Traversable[X]](config: DeserializationConfig,
                                                             javaType: JavaType,
                                                             companion: GenericCompanion[CC]) = {
    val elementType = javaType.containedType(0)
    new SeqDeserializer[CC](companion, elementType)
  }

  private def createOptionDeserializer(config: DeserializationConfig,
                                       javaType: JavaType) = {
    val elementType = javaType.containedType(0)
    new OptionDeserializer(elementType)
  }

  private def createImmutableMapDeserializer[CC[A, B] <: Map[A, B] with MapLike[A, B, CC[A, B]]](config: DeserializationConfig,
                                                                                        javaType: JavaType,
                                                                                        companion: MapFactory[CC]) = {
    val keyType = javaType.containedType(0)
    val valueType = javaType.containedType(1)
    if (keyType.getRawClass == classOf[String]) {
      new ImmutableMapDeserializer[CC](companion, valueType)
    } else if (keyType.getRawClass == classOf[Int] || keyType.getRawClass == classOf[java.lang.Integer]) {
      new IntMapDeserializer(valueType)
    } else if (keyType.getRawClass == classOf[Long] || keyType.getRawClass == classOf[java.lang.Long]) {
      new LongMapDeserializer(valueType)
    } else {
      null
    }
  }
}
