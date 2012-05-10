package com.codahale.jerkson.deser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.{DeserializationConfig, DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.databind.node.TreeTraversingParser

class EitherDeserializer(config: DeserializationConfig,
                         javaType: JavaType) extends JsonDeserializer[Object] {
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
