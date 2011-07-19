package com.codahale.jerkson

import org.codehaus.jackson.JsonFactory
import org.codehaus.jackson.map.ObjectMapper

trait Factory {
  /**
   * The ObjectMapper to be used by {@link Parser} and {@link Generator}.
   */
  protected val mapper: ObjectMapper

  /**
   * The JsonFactory to be used by {@link Parser} and {@link Generator}.
   */
  protected val factory: JsonFactory
}
