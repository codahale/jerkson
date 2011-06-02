package com.codahale.jerkson.deser

import scala.collection.JavaConversions._
import org.codehaus.jackson.`type`.JavaType
import collection.mutable.ArrayBuffer
import org.codehaus.jackson.map._
import org.codehaus.jackson.{JsonNode, JsonToken, JsonParser}
import com.codahale.jerkson.util._
import org.codehaus.jackson.node.{ObjectNode, NullNode, TreeTraversingParser}
import org.codehaus.jackson.map.annotate.JsonCachable

@JsonCachable
class CaseClassDeserializer(config: DeserializationConfig,
                            javaType: JavaType,
                            provider: DeserializerProvider) extends JsonDeserializer[Object] {
  require(javaType.getRawClass.getConstructors.length == 1, "Case classes must only have one constructor.")
  private val constructor = javaType.getRawClass.getConstructors.head
  private val params = CaseClassSigParser.parse(javaType.getRawClass).toArray

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): Object = {
    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken != JsonToken.FIELD_NAME &&
      jp.getCurrentToken != JsonToken.END_OBJECT) {
      throw ctxt.mappingException(javaType.getRawClass)
    }

    val node = jp.readValueAsTree

    val values = new ArrayBuffer[AnyRef]
    for ((paramName, paramType) <- params) {
      val field = node.get(paramName)
      val tp = new TreeTraversingParser(if (field == null) NullNode.getInstance else field, jp.getCodec)
      val value = if (paramType.getRawClass == classOf[Option[_]]) {
        // thanks again for special-casing VALUE_NULL
        Option(tp.getCodec.readValue[Object](tp, paramType.containedType(0)))
      } else {
        tp.getCodec.readValue[Object](tp, paramType)
      }

      if (field != null || value != null) {
        values += value
      }


      if (values.size == params.size) {
        return constructor.newInstance(values.toArray: _*).asInstanceOf[Object]
      }
    }

    throw new JsonMappingException(errorMessage(node))
  }

  private def errorMessage(node: JsonNode) = {
    val names = params.map {_._1}.mkString("[", ", ", "]")
    val existing = node match {
      case obj: ObjectNode => obj.getFieldNames.mkString("[", ", ", "]")
      case _: NullNode => "[]" // this is what Jackson deserializes the inside of an empty object to
      case unknown => "a non-object"
    }
    "Invalid JSON. Needed %s, but found %s.".format(names, existing)
  }
}
