package com.codahale.jerkson

object AST {
  sealed trait JValue {
    def value : Any
    
    def valueAs[A] : A = value.asInstanceOf[A]
    
    def \(fieldName : String) : Option[JValue] = None
    
    def apply(idx : Int) : Option[JValue] = None
    
    def \\(fieldName : String) : Seq[JValue] = Nil
  }

  case object JNull extends JValue {
    def value = null
  }

  case class JBoolean(value: Boolean) extends JValue

  case class JInt(value: BigInt) extends JValue

  case class JFloat(value: Double) extends JValue

  case class JString(value: String) extends JValue

  case class JArray(elements: List[JValue]) extends JValue {
    def value = null
    
    override def apply(index : Int) : Option[JValue] = {
      try {
        Some(elements(index))
      } catch {
        case _ => None
      }
    }
  }

  case class JField(name: String, value: JValue) extends JValue

  case class JObject(fields: List[JField]) extends JValue {
    def value = null
    
    override def \(fieldName : String) : Option[JValue] = {
      fields.find { case JField(name, _) =>
        name == fieldName
      }.map { case JField(_, value) =>
        value
      }
    }
    
    override def \\(fieldName : String) : Seq[JValue] = {
      fields.flatMap { 
        case JField(name, value) if name == fieldName => Seq(value) ++ (value \\ fieldName)
        case JField(_, value) => value \\ fieldName
      }
    }
  }
}
