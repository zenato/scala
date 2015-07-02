package io.teamscala.scala.util

import scala.reflect.runtime.{universe => ru}

object TypeTagUtils {
  @inline def typeTag[T: ru.TypeTag](obj: T): ru.TypeTag[T] = ru.typeTag[T]
  @inline def typeTag[T: ru.TypeTag](cls: Class[T]): ru.TypeTag[T] = ru.typeTag[T]

  @inline def typeOf[T: ru.TypeTag](obj: T): ru.Type = ru.typeOf[T]
  @inline def typeOf[T: ru.TypeTag](cls: Class[T]): ru.Type = ru.typeOf[T]

  @inline def classTag[T: scala.reflect.ClassTag](obj: T): scala.reflect.ClassTag[T] = scala.reflect.classTag[T]
  @inline def classTag[T: scala.reflect.ClassTag](cls: Class[T]): scala.reflect.ClassTag[T] = scala.reflect.classTag[T]

  @inline def tagToClass[T](tag: scala.reflect.ClassTag[T]): Class[T] = tag.runtimeClass.asInstanceOf[Class[T]]

  @inline def typeToClass[T: scala.reflect.ClassTag]: Class[T] = tagToClass(scala.reflect.classTag[T])
}

