package com.codahale.jerkson.deser

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import com.codahale.jerkson.JsonSnakeCase
import com.codahale.jerkson.util._
import com.codahale.jerkson.Util._
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.node.{ObjectNode, NullNode, TreeTraversingParser}
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.core.{JsonToken, JsonParser}

class CaseClassDeserializer(config: DeserializationConfig,
                            javaType: JavaType,
                            classLoader: ClassLoader) extends JsonDeserializer[Object] {
  private val isSnakeCase = javaType.getRawClass.isAnnotationPresent(classOf[JsonSnakeCase])
  private val params = CaseClassSigParser.parse(javaType.getRawClass, config.getTypeFactory, classLoader).map {
    case (name, jt) => (if (isSnakeCase) snakeCase(name) else name, jt)
  }.toArray
  private val paramTypes = params.map { _._2.getRawClass }.toList
  private val constructor = javaType.getRawClass.getConstructors.find { c =>
    val constructorTypes = c.getParameterTypes.toList.map { t =>
      t.toString match {
        case "byte" => classOf[java.lang.Byte]
        case "short" => classOf[java.lang.Short]
        case "int" => classOf[java.lang.Integer]
        case "long" => classOf[java.lang.Long]
        case "float" => classOf[java.lang.Float]
        case "double" => classOf[java.lang.Double]
        case "char" => classOf[java.lang.Character]
        case "boolean" => classOf[java.lang.Boolean]
        case _ => t
      }
    }
    constructorTypes == paramTypes
  }.getOrElse { throw new JsonMappingException("Unable to find a case accessor for " + javaType.getRawClass.getName) }

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): Object = {
    if (jp.getCurrentToken == JsonToken.START_OBJECT) {
      jp.nextToken()
    }

    if (jp.getCurrentToken != JsonToken.FIELD_NAME &&
      jp.getCurrentToken != JsonToken.END_OBJECT) {
      throw ctxt.mappingException(javaType.getRawClass)
    }

    val node = jp.readValueAsTree[JsonNode]

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
    val names = params.map { _._1 }.mkString("[", ", ", "]")
    val existing = node match {
      case obj: ObjectNode => obj.fieldNames.mkString("[", ", ", "]")
      case _: NullNode => "[]" // this is what Jackson deserializes the inside of an empty object to
      case unknown => "a non-object"
    }
    "Invalid JSON. Needed %s, but found %s.".format(names, existing)
  }

  override def isCachable = true
}
