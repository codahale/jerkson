package com.codahale.jerkson.deser

import org.codehaus.jackson.`type`.JavaType
import com.thoughtworks.paranamer.{BytecodeReadingParanamer, CachingParanamer}
import org.codehaus.jackson.map.`type`.TypeFactory
import org.codehaus.jackson.node.{NullNode, TreeTraversingParser}
import collection.mutable.ArrayBuffer
import org.codehaus.jackson.map._
import com.codahale.jerkson.Json
import org.codehaus.jackson.{JsonNode, JsonToken, JsonParser}

object CaseClassDeserializer {
  val paranamer = new CachingParanamer(new BytecodeReadingParanamer)
}

class CaseClassDeserializer(config: DeserializationConfig,
                            javaType: JavaType,
                            provider: DeserializerProvider) extends JsonDeserializer[Object] {
  import CaseClassDeserializer._

  val constructors = javaType.getRawClass.getConstructors.map { constructor =>
    val names = paranamer.lookupParameterNames(constructor).toSeq
    val types = constructor.getGenericParameterTypes.toSeq
    names.zip(types) -> constructor
  }.toSeq


  def deserialize(jp: JsonParser, ctxt: DeserializationContext): Object = {
    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken() != JsonToken.FIELD_NAME &&
      jp.getCurrentToken != JsonToken.END_OBJECT) {
      throw ctxt.mappingException(javaType.getRawClass)
    }

    val node = jp.readValueAsTree

    for ((params, constructor) <- constructors) {
      val values = new ArrayBuffer[AnyRef]
      for ((name, paramType) <- params) {
        val field = node.get(name)
        val tp = new TreeTraversingParser(if (field == null) NullNode.getInstance else field, jp.getCodec)
        val javaType = TypeFactory.`type`(paramType)
        val value = if (javaType.getRawClass == classOf[Option[_]]) {
          // thanks again for special-casing VALUE_NULL
          Option(tp.getCodec.readValue[Object](tp, javaType.containedType(0)))
        } else {
          tp.getCodec.readValue[Object](tp, javaType)
        }

        if (field != null || value != null) {
          values += value
        }

      }

      if (values.size == params.size) {
        return constructor.newInstance(values.toArray: _*).asInstanceOf[Object]
      }
    }

    throw new JsonMappingException(errorMessage(node))
  }

  private def errorMessage(node: JsonNode) = {
    val names = constructors.map { case (params, _) => params.map { case (name, _) => name }.mkString("[", ", ", "]") }
    val options = names match {
      case Seq(name) => name
      case l => l.mkString("either ", " or ", "")
    }
    val existing = Json.parse[Map[String, JsonNode]](node).keys.mkString("[", ", ", "]")
    "Invalid JSON. Needed %s, but found %s.".format(options, existing)
  }
}
