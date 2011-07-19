package com.codahale.jerkson

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.`type`.{TypeFactory, ArrayType}

private[jerkson] object Types {
  def build[A](factory: TypeFactory, manifest: Manifest[A]): JavaType = {
    if (manifest.erasure.isArray) {
      ArrayType.construct(factory.constructType(manifest.erasure.getComponentType))
    } else {
      factory.constructParametricType(
        manifest.erasure,
        manifest.typeArguments.map { m => build(factory, m)}.toArray: _*)
    }
  }
}
