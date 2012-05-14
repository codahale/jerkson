package com.codahale.jerkson

import deser.ScalaDeserializers
import com.fasterxml.jackson.databind.Module.SetupContext
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import ser.ScalaSerializers

class ScalaModule(classLoader: ClassLoader) extends Module {
  def version = new Version(0, 6, 0, "SNAPSHOT", "com.codahale", "jerkson")
  def getModuleName = "jerkson"

  def setupModule(context: SetupContext) {
    context.addDeserializers(new ScalaDeserializers(classLoader, context))
    context.addSerializers(new ScalaSerializers)
  }
}
