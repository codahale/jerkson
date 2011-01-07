package com.codahale.jerkson.ser

import org.codehaus.jackson.map.ser.BeanSerializerFactory
import org.codehaus.jackson.map.introspect.BasicBeanDescription
import com.codahale.jerkson.AST.JValue
import org.codehaus.jackson.map.{JsonSerializer, SerializationConfig}

/**
 *
 * @author coda
 */
class ScalaSerializerFactory extends BeanSerializerFactory {
  override def constructBeanSerializer(config: SerializationConfig, beanDesc: BasicBeanDescription) = {
    val ser = if (classOf[Seq[_]].isAssignableFrom(beanDesc.getBeanClass)) {
      new SeqSerializer
    } else if (classOf[Map[_,_]].isAssignableFrom(beanDesc.getBeanClass)) {
      new MapSerializer
    } else if (classOf[JValue].isAssignableFrom(beanDesc.getBeanClass)) {
      new JValueSerializer
    } else if (classOf[Either[_,_]].isAssignableFrom(beanDesc.getBeanClass)) {
      new EitherSerializer
    } else if (classOf[Product].isAssignableFrom(beanDesc.getBeanClass)) {
      new CaseClassSerializer(beanDesc.getBeanClass)
    } else {
      super.constructBeanSerializer(config, beanDesc)
    }
    ser.asInstanceOf[JsonSerializer[Object]]
  }
}
