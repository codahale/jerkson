package com.codahale.jerkson.deser

import collection.JavaConversions._
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.core.{JsonToken, JsonParser}
import com.fasterxml.jackson.databind.{JsonMappingException, DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.databind.node.{IntNode, BooleanNode, NullNode, ObjectNode}

class RangeDeserializer extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext) = {
    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken != JsonToken.FIELD_NAME &&
      jp.getCurrentToken != JsonToken.END_OBJECT) {
      throw ctxt.mappingException(classOf[Range])
    }

    val node = jp.readValueAsTree[JsonNode]
    val inclusiveNode  = node.get("inclusive")
    val stepNode = node.get("step")
    val startNode = node.get("start")
    val endNode = node.get("end")

    if (startNode == null || !classOf[IntNode].isAssignableFrom(startNode.getClass) ||
        endNode == null || !classOf[IntNode].isAssignableFrom(endNode.getClass)) {
      throw new JsonMappingException(errorMessage(node))
    }

    val step = if (stepNode == null || !classOf[IntNode].isAssignableFrom(stepNode.getClass)) {
      1
    } else {
      stepNode.intValue
    }

    val start = startNode.asInstanceOf[IntNode].intValue
    val end = endNode.asInstanceOf[IntNode].intValue

    if (inclusiveNode == null || inclusiveNode == BooleanNode.FALSE) {
      Range(start, end, step)
    } else {
      Range.inclusive(start, end, step)
    }
  }

  private def errorMessage(node: JsonNode) = {
    val existing = node match {
      case obj: ObjectNode => obj.fieldNames.mkString("[", ", ", "]")
      case _: NullNode => "[]" // this is what Jackson deserializes the inside of an empty object to
      case unknown => "a non-object"
    }
    "Invalid JSON. Needed [start, end, <step>, <inclusive>], but found %s.".format(existing)
  }

  override def isCachable = true
}
