package com.simple.jerkson

private[jerkson] object Util {
  /**
   * Convert a string from camelCase to snake_case.
   */
  def snakeCase(word: String) = {
    val len = word.length()
    val builder = new StringBuilder(len)
    if (len > 0) {
      var idx = if (word(0) == '_') {
        // preserve the first underscore
        builder += '_'
        1
      } else 0
      while (idx < len) {
        val char = word(idx)
        if (Character.isUpperCase(char)) {
          builder += '_'
          builder += Character.toLowerCase(char)
        } else {
          builder += char
        }
        idx += 1
      }
    }
    builder.toString()
  }
}
