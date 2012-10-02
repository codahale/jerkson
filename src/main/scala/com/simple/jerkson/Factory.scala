package com.codahale.jerkson

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper

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
