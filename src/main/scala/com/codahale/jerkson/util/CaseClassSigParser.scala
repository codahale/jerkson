package com.codahale.jerkson.util

import com.codahale.jerkson.util.scalax.rules.scalasig._
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map.`type`.TypeFactory

class MissingPickledSig(clazz: Class[_]) extends Error("Failed to parse pickled Scala signature from: %s".format(clazz))

class MissingExpectedType(clazz: Class[_]) extends Error(
  "Parsed pickled Scala signature, but no expected type found: %s"
  .format(clazz)
)

object CaseClassSigParser {
  val SCALA_SIG = "ScalaSig"
  val SCALA_SIG_ANNOTATION = "Lscala/reflect/ScalaSignature;"
  val BYTES_VALUE = "bytes"

  protected def parseScalaSig[A](clazz: Class[A]): Option[ScalaSig] = {
    val firstPass = ScalaSigParser.parse(clazz)
    firstPass match {
      case Some(x) => {
        Some(x)
      }
      case None if clazz.getName.endsWith("$") => {
        val clayy = Class.forName(clazz.getName.replaceFirst("\\$$", ""))
        val secondPass = ScalaSigParser.parse(clayy)
        secondPass
      }
      case x => x
    }
  }

  protected def findSym[A](clazz: Class[A]) = {
    val pss = parseScalaSig(clazz)
    pss match {
      case Some(x) => {
        val topLevelClasses = x.topLevelClasses
        topLevelClasses.headOption match {
          case Some(tlc) => {
            tlc
          }
          case None => {
            val topLevelObjects = x.topLevelObjects
            topLevelObjects.headOption match {
              case Some(tlo) => {
                tlo
              }
              case _ => throw new MissingExpectedType(clazz)
            }
          }
        }
      }
      case None => throw new MissingPickledSig(clazz)
    }
  }

  def parse[A](clazz: Class[A]) = {
    findSym(clazz).children
      .filter(c => c.isCaseAccessor && !c.isPrivate)
      .map(_.asInstanceOf[MethodSymbol])
      .zipWithIndex
      .flatMap {
        case (ms, idx) => {
          ms.infoType match {
            case NullaryMethodType(t: TypeRefType) => Some(ms.name -> typeRef2JavaType(t))
            case _ => None
          }
        }
      }
  }

  protected def typeRef2JavaType(ref: TypeRefType): JavaType = {
    try {
      val klass = loadClass(ref.symbol.path)
      TypeFactory.parametricType(
        klass, ref.typeArgs.map {
          t => typeRef2JavaType(t.asInstanceOf[TypeRefType])
        }: _*
      )
    } catch {
      case e: Throwable => {
        e.printStackTrace()
        null
      }
    }
  }

  protected def loadClass(path: String) = path match {
    case "scala.Predef.Map" => classOf[Map[_, _]]
    case "scala.Predef.Set" => classOf[Set[_]]
    case "scala.Predef.String" => classOf[String]
    case "scala.Long" => classOf[java.lang.Long]
    case "scala.Int" => classOf[java.lang.Integer]
    case name => Class.forName(name)
  }
}
