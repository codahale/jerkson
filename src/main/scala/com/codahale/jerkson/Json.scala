package com.codahale.jerkson

import io.Source
import deser.ScalaDeserializerFactory
import ser.ScalaSerializerFactory
import org.codehaus.jackson.map.deser.StdDeserializerProvider
import org.codehaus.jackson.map.{MappingJsonFactory, ObjectMapper}
import java.net.URL
import java.io._
import org.codehaus.jackson.map.`type`.TypeFactory
import org.codehaus.jackson.`type`.JavaType
import com.codahale.jerkson.AST.{JValue, JNull}
import org.codehaus.jackson.node.TreeTraversingParser
import org.codehaus.jackson.{JsonProcessingException, JsonNode, JsonToken, JsonEncoding, JsonGenerator, JsonParser => JacksonParser}

object Json {
  private val deserializerFactory = new ScalaDeserializerFactory
  private val serializerFactory = new ScalaSerializerFactory

  private val mapper = new ObjectMapper
  mapper.setSerializerFactory(serializerFactory)
  mapper.setDeserializerProvider(new StdDeserializerProvider(deserializerFactory))
  
  private val factory = new MappingJsonFactory(mapper)
  factory.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)
  factory.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
  factory.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES)
  factory.enable(JacksonParser.Feature.ALLOW_COMMENTS)
  factory.enable(JacksonParser.Feature.AUTO_CLOSE_SOURCE)

  /**
   * Parse a JSON string as a particular type.
   */
  def parse[A](input: String)(implicit mf: Manifest[A]): A = parse[A](factory.createJsonParser(input), mf)

  /**
   * Parse a JSON input stream as a particular type.
   */
  def parse[A](input: InputStream)(implicit mf: Manifest[A]): A = parse[A](factory.createJsonParser(input), mf)

  /**
   * Parse a JSON file as a particular type.
   */
  def parse[A](input: File)(implicit mf: Manifest[A]): A = parse[A](factory.createJsonParser(input), mf)

  /**
   * Parse a JSON URL as a particular type.
   */
  def parse[A](input: URL)(implicit mf: Manifest[A]): A = parse[A](factory.createJsonParser(input), mf)

  /**
   * Parse a JSON Reader as a particular type.
   */
  def parse[A](input: Reader)(implicit mf: Manifest[A]): A = parse[A](factory.createJsonParser(input), mf)

  /**
   * Parse a JSON byte array as a particular type.
   */
  def parse[A](input: Array[Byte])(implicit mf: Manifest[A]): A = parse[A](factory.createJsonParser(input), mf)

  /**
   * Parse a JSON Source as a particular type.
   */
  def parse[A](input: Source)(implicit mf: Manifest[A]): A = parse[A](input.mkString)

  /**
   * Parse a JSON node as a particular type.
   */
  def parse[A](input: JsonNode)(implicit mf: Manifest[A]): A = {
    val parser = new TreeTraversingParser(input, mapper)
    parse(parser, mf)
  }

  /**
   * Parse a streaming JSON array of particular types, passing each deserialized
   * object to a callback method.
   */
  def parseStreamOf[A](input: InputStream)(callback: A => Unit)(implicit mf: Manifest[A]) {
    val parser = factory.createJsonParser(input)

    if (parser.nextToken() != JsonToken.START_ARRAY) {
      throw new IllegalArgumentException("JSON stream is not an array")
    }

    while (parser.nextToken != JsonToken.END_ARRAY) {
      callback(parse(parser, mf))
    }
  }

  /**
   * Generate JSON from the given object and return it as a string.
   */
  def generate[A](obj: A): String = {
    val writer = new StringWriter
    generate(obj, writer)
    writer.toString
  }

  /**
   * Generate JSON from the given object and write to the given Writer.
   */
  def generate[A](obj: A, output: Writer) {
    generate(obj, factory.createJsonGenerator(output))
  }

  /**
   * Generate JSON from the given object and write it to the given OutputStream.
   */
  def generate[A](obj: A, output: OutputStream) {
    generate(obj, factory.createJsonGenerator(output, JsonEncoding.UTF8))
  }

  /**
   * Generate JSON from the given object and write it to the given File.
   */
  def generate[A](obj: A, output: File) {
    generate(obj, factory.createJsonGenerator(output, JsonEncoding.UTF8))
  }

  private def generate[A](obj: A, generator: JsonGenerator) {
    generator.writeObject(obj)
    generator.close()
  }

  private[jerkson] def manifest2JavaType[A](manifest: Manifest[A]): JavaType = {
    if (manifest.erasure.isArray) {
      throw new IllegalArgumentException("can't handle arrays")
    }
    TypeFactory.parametricType(manifest.erasure,
                               manifest.typeArguments
                                 .map{m => manifest2JavaType(m)}.toArray: _*)
  }

  private def parse[A](parser: JacksonParser, mf: Manifest[A]): A = {
    try {
      if (mf.erasure == classOf[Option[_]]) {
        // thanks for special-casing VALUE_NULL, guys
        Option(parse(parser, mf.typeArguments.head)).asInstanceOf[A]
      } else if (mf.erasure == classOf[JValue]) {
        val value: A = parser.getCodec.readValue(parser, manifest2JavaType(mf))
        if (value == null) JNull.asInstanceOf[A] else value
      } else {
        parser.getCodec.readValue(parser, manifest2JavaType(mf))
      }
    } catch {
      case e: JsonProcessingException => throw new ParsingException(e)
    }
  }
}
