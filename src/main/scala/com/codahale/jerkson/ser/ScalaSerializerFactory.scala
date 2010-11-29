package com.codahale.jerkson.ser

import org.codehaus.jackson.map.ser.BeanSerializerFactory
import org.codehaus.jackson.map.introspect.BasicBeanDescription
import org.codehaus.jackson.map.SerializationConfig

/**
 *
 * @author coda
 */
class ScalaSerializerFactory extends BeanSerializerFactory {
  override def constructBeanSerializer(config: SerializationConfig, beanDesc: BasicBeanDescription) = {
    if (classOf[Seq[_]].isAssignableFrom(beanDesc.getBeanClass)) {
      new SeqSerializer
    } else if (classOf[Map[_,_]].isAssignableFrom(beanDesc.getBeanClass)) {
      new MapSerializer
    } else if (classOf[Product].isAssignableFrom(beanDesc.getBeanClass)) {
      new CaseClassSerializer
    } else {
      super.constructBeanSerializer(config, beanDesc)
    }
  }
}
