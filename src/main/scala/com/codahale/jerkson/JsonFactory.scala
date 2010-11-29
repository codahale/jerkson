package com.codahale.jerkson

import deser.ScalaDeserializerFactory
import ser.ScalaSerializerFactory
import org.codehaus.jackson.map.deser.StdDeserializerProvider
import org.codehaus.jackson.map.{MappingJsonFactory, ObjectMapper}

object JsonFactory {
  private val deserializerFactory = new ScalaDeserializerFactory
  private val serializerFactory = new ScalaSerializerFactory

  private val mapper = new ObjectMapper
  mapper.setSerializerFactory(serializerFactory)
  mapper.setDeserializerProvider(new StdDeserializerProvider(deserializerFactory))
  
  private[jerkson] val factory = new MappingJsonFactory(mapper)
}
