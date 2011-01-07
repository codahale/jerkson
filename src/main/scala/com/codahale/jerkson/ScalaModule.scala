package com.codahale.jerkson

import deser.ScalaDeserializers
import org.codehaus.jackson.map.Module.SetupContext
import org.codehaus.jackson.Version
import org.codehaus.jackson.map.Module
import ser.ScalaSerializers

/**
 *
 * @author coda
 */
class ScalaModule extends Module {
  def version = new Version(0, 2, 0, "")
  def getModuleName = "jerkson"

  def setupModule(context: SetupContext) = {
    context.addDeserializers(new ScalaDeserializers)
    context.addSerializers(new ScalaSerializers)
  }
}
