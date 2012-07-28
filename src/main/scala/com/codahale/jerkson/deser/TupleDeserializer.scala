package com.codahale.jerkson.deser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.node.TreeTraversingParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.`type`.SimpleType
import com.fasterxml.jackson.databind.JavaType
import com.codahale.jerkson.ser.TupleSerializer

class TupleDeserializer(javaType: JavaType) extends JsonDeserializer[Product] {
  val types = for (i <- 0 until javaType.containedTypeCount()) yield javaType.containedType(i)

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): Product = {
    if (jp.getCurrentToken != JsonToken.START_ARRAY) {
      throw ctxt.mappingException("Tuple expected as an array")
    }

    val builder = collection.mutable.ListBuffer[Object]()
    
    for (t <- types) {
      jp.nextToken()
      val elementDeserializer = ctxt.findRootValueDeserializer(t)
      builder += elementDeserializer.deserialize(jp, ctxt)
    }
    
    if (jp.nextToken() != JsonToken.END_ARRAY) {
      throw ctxt.mappingException("More elements than expected for Tuple" + types.size)
    }
    
    val c: Class[_ <: Product] = TupleSerializer.allTupleClasses(types.size - 1)
    
    val consArgs = for (n <- 1 to types.size) yield classOf[Object]
      
    c.getConstructor(consArgs: _*).newInstance(builder.toArray: _*)
  }
}
