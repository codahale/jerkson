package com.codahale.jerkson

import io.Source
import java.net.URL
import com.codahale.jerkson.AST.{JValue, JNull}
import org.codehaus.jackson.{JsonNode, JsonParser, JsonProcessingException}
import org.codehaus.jackson.node.TreeTraversingParser
import java.io.{EOFException, Reader, File, InputStream}

trait Parser extends Factory {
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
   * Parse a streaming JSON array of particular types, returning an iterator
   * of the elements of the stream.
   */
  def stream[A](input: InputStream)(implicit mf: Manifest[A]): Iterator[A] = {
    val parser = factory.createJsonParser(input)
    new StreamingIterator[A](parser, mf)
  }

  /**
   * Parse a streaming JSON array of particular types, passing each deserialized
   * object to a callback method.
   */
  @deprecated("Use Json.stream instead")
  // TODO: 2/18/11 <coda> -- remove for 0.2.0
  def parseStreamOf[A](input: InputStream)(callback: A => Unit)(implicit mf: Manifest[A]) {
    stream[A](input)(mf).foreach(callback)
  }

  private[jerkson] def parse[A](parser: JsonParser, mf: Manifest[A]): A = {
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
      case e: JsonProcessingException => throw ParsingException(e)
      case e: EOFException => throw new ParsingException("JSON document ended unexpectedly.", e)
    }
  }
}
