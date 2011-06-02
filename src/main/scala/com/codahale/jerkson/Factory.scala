package com.codahale.jerkson

import org.codehaus.jackson.JsonFactory
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.`type`.TypeFactory
import org.codehaus.jackson.`type`.JavaType

trait Factory {
  /**
   * The ObjectMapper to be used by {@link Parser} and {@link Generator}.
   */
  protected val mapper: ObjectMapper

  /**
   * The JsonFactory to be used by {@link Parser} and {@link Generator}.
   */
  protected val factory: JsonFactory

  private[jerkson] def manifest2JavaType[A](manifest: Manifest[A]): JavaType = {
    if (manifest.erasure.isArray) {
      TypeFactory.arrayType(manifest.erasure.getComponentType)
    } else {
      TypeFactory.parametricType(manifest.erasure,
        manifest.typeArguments
          .map {m => manifest2JavaType(m)}.toArray: _*)
    }
  }
}
