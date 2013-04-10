package com.codahale.jerkson.util

import com.codahale.jerkson.util.scalax.rules.scalasig._
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.`type`.TypeFactory
import scala.reflect.ScalaSignature
import scala.reflect.internal.pickling.ByteCodecs

class MissingPickledSig(clazz: Class[_]) extends Error("Failed to parse pickled Scala signature from: %s".format(clazz))

class MissingExpectedType(clazz: Class[_]) extends Error(
  "Parsed pickled Scala signature, but no expected type found: %s"
  .format(clazz)
)

object CaseClassSigParser {
  val SCALA_SIG = "ScalaSig"
  val SCALA_SIG_ANNOTATION = "Lscala/reflect/ScalaSignature;"
  val BYTES_VALUE = "bytes"

  private def parseClassFileFromByteCode(clazz: Class[_]): Option[ClassFile] = try {
    // taken from ScalaSigParser parse method with the explicit purpose of walking away from NPE
    val byteCode = ByteCode.forClass(clazz)
    Option(ClassFileParser.parse(byteCode))
  }
  catch {
    case e: NullPointerException => None // yes, this is the exception, but it is totally unhelpful to the end user
  }

  private def parseByteCodeFromAnnotation(clazz: Class[_]): Option[ByteCode] = {
    if (clazz.isAnnotationPresent(classOf[ScalaSignature])) {
      val sig = clazz.getAnnotation(classOf[ScalaSignature])
      val bytes = sig.bytes.getBytes("UTF-8")
      val len = ByteCodecs.decode(bytes)
      Option(ByteCode(bytes.take(len)))
    } else {
      None
    }
  }

  private def parseScalaSig(_clazz: Class[_], classLoader: ClassLoader): Option[ScalaSig] = {
    val clazz = findRootClass(_clazz, classLoader)
    parseClassFileFromByteCode(clazz).map(ScalaSigParser.parse(_)).getOrElse(None) orElse
      parseByteCodeFromAnnotation(clazz).map(ScalaSigAttributeParsers.parse(_)) orElse
      None
  }

  protected def findRootClass(klass: Class[_], classLoader: ClassLoader) =
    loadClass(klass.getName.split("\\$").head, classLoader)

  protected def simpleName(klass: Class[_]) =
    klass.getName.split("\\$").last

  protected def findSym[A](clazz: Class[A], classLoader: ClassLoader) = {
    val name = simpleName(clazz)
    val pss = parseScalaSig(clazz, classLoader)
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
                x.symbols.find { s => !s.isModule && s.name == name } match {
                  case Some(s) => s.asInstanceOf[ClassSymbol]
                  case None => throw new MissingExpectedType(clazz)
                }
              }
              case _ => throw new MissingExpectedType(clazz)
            }
          }
        }
      }
      case None => throw new MissingPickledSig(clazz)
    }
  }

  def parse[A](clazz: Class[A], factory: TypeFactory, classLoader: ClassLoader) = {
    findSym(clazz, classLoader).children.filter(c => c.isCaseAccessor && !c.isPrivate)
      .flatMap { ms =>
        ms.asInstanceOf[MethodSymbol].infoType match {
          case NullaryMethodType(t: TypeRefType) => ms.name -> typeRef2JavaType(t, factory, classLoader) :: Nil
          case _ => Nil
        }
      }
  }

  protected def typeRef2JavaType(ref: TypeRefType, factory: TypeFactory, classLoader: ClassLoader): JavaType = {
    try {
      if (ref.symbol.path == "scala.Array") {
        val elementType = typeRef2JavaType(ref.typeArgs.head.asInstanceOf[TypeRefType], factory, classLoader)
        val realElementType = elementType.getRawClass.getName match {
          case "java.lang.Boolean" => classOf[Boolean]
          case "java.lang.Byte" => classOf[Byte]
          case "java.lang.Character" => classOf[Char]
          case "java.lang.Double" => classOf[Double]
          case "java.lang.Float" => classOf[Float]
          case "java.lang.Integer" => classOf[Int]
          case "java.lang.Long" => classOf[Long]
          case "java.lang.Short" => classOf[Short]
          case _ => elementType.getRawClass
        }

        val array = java.lang.reflect.Array.newInstance(realElementType, 0)
        factory.constructType(array.getClass)
      } else {
        val klass = loadClass(ref.symbol.path, classLoader)
        factory.constructParametricType(
          klass, ref.typeArgs.map {
            t => typeRef2JavaType(t.asInstanceOf[TypeRefType], factory, classLoader)
          }: _*
        )
      }
    } catch {
      case e: Throwable => {
        e.printStackTrace()
        null
      }
    }
  }

  protected def loadClass(path: String, classLoader: ClassLoader) = path match {
    case "scala.Predef.Map" => classOf[Map[_, _]]
    case "scala.Predef.Set" => classOf[Set[_]]
    case "scala.Predef.String" => classOf[String]
    case "scala.package.List" => classOf[List[_]]
    case "scala.package.Seq" => classOf[Seq[_]]
    case "scala.package.Sequence" => classOf[Seq[_]]
    case "scala.package.Collection" => classOf[Seq[_]]
    case "scala.package.IndexedSeq" => classOf[IndexedSeq[_]]
    case "scala.package.RandomAccessSeq" => classOf[IndexedSeq[_]]
    case "scala.package.Iterable" => classOf[Iterable[_]]
    case "scala.package.Iterator" => classOf[Iterator[_]]
    case "scala.package.Vector" => classOf[Vector[_]]
    case "scala.package.BigDecimal" => classOf[BigDecimal]
    case "scala.package.BigInt" => classOf[BigInt]
    case "scala.package.Integer" => classOf[java.lang.Integer]
    case "scala.package.Character" => classOf[java.lang.Character]
    case "scala.Long" => classOf[java.lang.Long]
    case "scala.Int" => classOf[java.lang.Integer]
    case "scala.Boolean" => classOf[java.lang.Boolean]
    case "scala.Short" => classOf[java.lang.Short]
    case "scala.Byte" => classOf[java.lang.Byte]
    case "scala.Float" => classOf[java.lang.Float]
    case "scala.Double" => classOf[java.lang.Double]
    case "scala.Char" => classOf[java.lang.Character]
    case "scala.Any" => classOf[Any]
    case "scala.AnyRef" => classOf[AnyRef]
    case name => classLoader.loadClass(name)
  }
}
