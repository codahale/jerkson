package com.codahale.jerkson.util

import com.codahale.jerkson.util.scalax.rules.scalasig._
import java.lang.annotation.Annotation
import java.lang.reflect.AnnotatedElement
import reflect.ScalaSignature
import reflect.generic.ByteCodecs

/** This is a helper object borrowed from Salat that looks up the Scala signature
  * from the classfile object's attribute first before looking for the .class
  * file and parsing bytecode from there to be able to work with the Play framework. */
object ScalaSigUtil {

  implicit def whatever2annotated(x: Any) = new PimpedAnnotatedElement(x)

  class PimpedAnnotatedElement(x: Any) {
    def annotation[A <: Annotation : Manifest]: Option[A] =
      x match {
        case x: AnnotatedElement if x != null => x.getAnnotation[A](manifest[A].erasure.asInstanceOf[Class[A]]) match {
          case a if a != null => Some(a)
          case _ => None
        }
        case _ => None
      }

    def annotated_?[A <: Annotation : Manifest]: Boolean = annotation[A](manifest[A]).isDefined
  }

  private def parseClassFileFromByteCode(clazz: Class[_]): Option[ClassFile] = try {
    // taken from ScalaSigParser parse method with the explicit purpose of walking away from NPE
    val byteCode  = ByteCode.forClass(clazz)
    Option(ClassFileParser.parse(byteCode))
  }
  catch {
    case e: NullPointerException => None  // yes, this is the exception, but it is totally unhelpful to the end user
  }

  private def parseByteCodeFromAnnotation(clazz: Class[_]): Option[ByteCode] = {
    clazz.annotation[ScalaSignature] match {
      case Some(sig) if sig != null => {
        val bytes = sig.bytes.getBytes("UTF-8")
        val len = ByteCodecs.decode(bytes)
        Option(ByteCode(bytes.take(len)))
      }
      case _ => None
    }
  }

  def parse(_clazz: Class[_]): Option[ScalaSig] = {
    // support case objects by selectively re-jiggering the class that has been passed in
    val clazz = if (_clazz.getName.endsWith("$")) Class.forName(_clazz.getName.replaceFirst("\\$$", "")) else _clazz
    assume(clazz != null, "parse: cannot parse ScalaSig from null class=%s".format(_clazz))

    parseClassFileFromByteCode(clazz).map(ScalaSigParser.parse(_)).getOrElse(None) orElse
    parseByteCodeFromAnnotation(clazz).map(ScalaSigAttributeParsers.parse(_)) orElse
    None
  }
}
