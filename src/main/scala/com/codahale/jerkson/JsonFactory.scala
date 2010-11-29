package com.codahale.jerkson

import deser.ScalaDeserializerFactory
import org.codehaus.jackson.{JsonFactory => JacksonFactory}
import org.codehaus.jackson.map.ser.StdSerializerProvider
import org.codehaus.jackson.map.deser.StdDeserializerProvider
import org.codehaus.jackson.map.{MappingJsonFactory, ObjectMapper}

object JsonFactory {
  private val deserializerFactory = new ScalaDeserializerFactory
  private val deserializerProvider = new StdDeserializerProvider(deserializerFactory)
  private val serializerProvider = new StdSerializerProvider()
  private val mapper = new ObjectMapper
  mapper.setSerializerProvider(serializerProvider)
  mapper.setDeserializerProvider(deserializerProvider)
  
  private[jerkson] val factory = new MappingJsonFactory(mapper)
}
