package com.codahale.jerkson

import deser.ScalaDeserializerFactory
import org.codehaus.jackson.{JsonFactory => JacksonFactory}
import org.codehaus.jackson.map.deser.StdDeserializerProvider
import org.codehaus.jackson.map.{MappingJsonFactory, ObjectMapper}
import org.codehaus.jackson.map.ser.{CustomSerializerFactory, StdSerializerProvider}
import ser.{ScalaSerializerFactory, CaseClassSerializer, MapSerializer, SeqSerializer}

object JsonFactory {
  private val deserializerFactory = new ScalaDeserializerFactory
  private val serializerFactory = new ScalaSerializerFactory

  private val mapper = new ObjectMapper
  mapper.setSerializerFactory(serializerFactory)
  mapper.setDeserializerProvider(new StdDeserializerProvider(deserializerFactory))
  
  private[jerkson] val factory = new MappingJsonFactory(mapper)
}
