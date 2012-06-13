package com.codahale.jerkson.ser
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.core.JsonGenerator

class TupleSerializer extends JsonSerializer[Product] {
  def serialize(value: Product, json: JsonGenerator, provider: SerializerProvider) {
    json.writeStartArray()
    for (v <- value.productIterator.toList) {
    	provider.defaultSerializeValue(v, json)
    }
    json.writeEndArray()
  }
}

object TupleSerializer {
  val allTupleClasses: List[Class[_]] = List(
      classOf[Tuple1[_]],
      classOf[Tuple2[_,_]],
      classOf[Tuple3[_,_,_]],
      classOf[Tuple4[_,_,_,_]],
      classOf[Tuple5[_,_,_,_,_]],
      classOf[Tuple6[_,_,_,_,_,_]],
      classOf[Tuple7[_,_,_,_,_,_,_]],
      classOf[Tuple8[_,_,_,_,_,_,_,_]],
      classOf[Tuple9[_,_,_,_,_,_,_,_,_]],
      classOf[Tuple10[_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple11[_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple12[_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple13[_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple14[_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
      classOf[Tuple22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]]
//      classOf[Tuple23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]],
  )
}
