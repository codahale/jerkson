package com.codahale.jerkson

import org.codehaus.jackson.{JsonParser => JacksonParser}
import org.codehaus.jackson.util.JsonParserDelegate
import org.codehaus.jackson.map.`type`.TypeFactory
import org.codehaus.jackson.`type`.JavaType

object JsonParser {
  import JsonFactory._

  def parse(json: String): JsonParser = new JsonParser(factory.createJsonParser(json))
}

class JsonParser private[jerkson] (parser: JacksonParser)
  extends JsonParserDelegate(parser) {
  
  def readValueAs[A](implicit mf: Manifest[A]): A = {
    if (mf.erasure == classOf[Option[_]]) {
      // thanks for special-casing VALUE_NULL, guys
      Option(readValueAs(mf.typeArguments.head)).asInstanceOf[A]
    } else {
      parser.getCodec.readValue(parser, manifest2Type(mf))
    }
  }

  private def manifest2Type[A](manifest: Manifest[A]): JavaType = {
    if (manifest.erasure.isArray) {
      throw new IllegalArgumentException("can't handle arrays")
    }
    TypeFactory.parametricType(manifest.erasure,
                               manifest.typeArguments
                                 .map { m => manifest2Type(m) }.toArray: _*)
  }
}
