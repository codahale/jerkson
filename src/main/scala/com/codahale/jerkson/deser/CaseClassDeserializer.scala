package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import com.thoughtworks.paranamer.{BytecodeReadingParanamer, CachingParanamer}
import org.codehaus.jackson.node.{NullNode, TreeTraversingParser}
import collection.mutable.ArrayBuffer
import org.codehaus.jackson.map._
import com.codahale.jerkson.Json
import org.codehaus.jackson.{JsonNode, JsonToken, JsonParser}
import com.codahale.jerkson.util._

object CaseClassDeserializer {
  val paranamer = new CachingParanamer(new BytecodeReadingParanamer)
}

class CaseClassDeserializer(config: DeserializationConfig,
                            javaType: JavaType,
                            provider: DeserializerProvider) extends JsonDeserializer[Object] {
  import CaseClassDeserializer._

  require(javaType.getRawClass.getConstructors.length == 1, "Case classes must only have one constructor.")
  private val constructor = javaType.getRawClass.getConstructors.head
  private val params = paranamer.lookupParameterNames(constructor).zip(CaseClassSigParser.parse(javaType.getRawClass)).toArray

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
    for ((name, javaType) <- params) {
      val field = node.get(name)
      val tp = new TreeTraversingParser(if (field == null) NullNode.getInstance else field, jp.getCodec)
      val value = if (javaType.getRawClass == classOf[Option[_]]) {
        // thanks again for special-casing VALUE_NULL
        Option(tp.getCodec.readValue[Object](tp, javaType.containedType(0)))
      } else {
        tp.getCodec.readValue[Object](tp, javaType)
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
    val existing = Json.parse[Map[String, JsonNode]](node).keys.mkString("[", ", ", "]")
    "Invalid JSON. Needed %s, but found %s.".format(names, existing)
  }
}
