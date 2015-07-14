package io.teamscala.scala

import java.io.{File, FilenameFilter}
import java.nio.charset.Charset
import java.util.Date

import scala.collection.GenTraversableLike

package object util {

  implicit def classOps[T](self: Class[T]): ClassOps[T] = new ClassOps(self)
  implicit def dateOps(self: Date): DateOps = new DateOps(self)
  implicit def fileOps(self: File): FileOps = new FileOps(self)
  implicit def hangulChar(self: Char): HangulChar = new HangulChar(self)
  implicit def hangulString(self: String): HangulString = new HangulString(self)
  implicit def productOps(self: Product): ProductOps = new ProductOps(self)
  implicit def stringOps(self: String): StringOps = new StringOps(self)
  implicit def throwableOps(self: Throwable): ThrowableOps = new ThrowableOps(self)
  implicit def traversableOps[A, Repr, Coll](self: Coll)(implicit ev1: Coll => GenTraversableLike[A, Repr]): TraversableOps[A, Repr] = new TraversableOps(self)

  @inline implicit def string2charset(charset: String): Charset = Charset.forName(charset)

  @inline implicit def fn2filenameFilter(fn: (File, String) => Boolean): FilenameFilter = new FilenameFilter {
    def accept(file: File, name: String) = fn(file, name)
  }

}
