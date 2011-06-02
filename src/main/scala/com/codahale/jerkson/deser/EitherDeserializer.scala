package com.codahale.jerkson.deser

import org.codehaus.jackson.JsonParser
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.{DeserializerProvider, DeserializationConfig, DeserializationContext, JsonDeserializer}
import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.node.TreeTraversingParser

@JsonCachable
class EitherDeserializer(config: DeserializationConfig,
                         javaType: JavaType,
                         provider: DeserializerProvider) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    val node = jp.readValueAsTree
    val tp = new TreeTraversingParser(node, jp.getCodec)

    try {
      Left(tp.getCodec.readValue[Object](tp, javaType.containedType(0)))
    } catch {
      case _ => Right(tp.getCodec.readValue[Object](tp, javaType.containedType(1)))
    }
  }
}
