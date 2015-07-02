package io.teamscala.scala

import java.io.{File, FilenameFilter}
import java.nio.charset.Charset
import java.util.Date

import scala.collection.TraversableLike

package object util {

  implicit def richClass[T](self: Class[T]): RichClass[T] = new RichClass(self)
  implicit def richDate(self: Date): RichDate = new RichDate(self)
  implicit def richFile(self: File): RichFile = new RichFile(self)
  implicit def richProduct(self: Product): RichProduct = new RichProduct(self)
  implicit def richString(self: String): RichString = new RichString(self)
  implicit def richThrowable(self: Throwable): RichThrowable = new RichThrowable(self)
  implicit def richTraversableLike[A, Repr](self: TraversableLike[A, Repr]): RichTraversableLike[A, Repr] = new RichTraversableLike(self)
  implicit def hangulChar(self: Char): HangulChar = new HangulChar(self)
  implicit def hangulString(self: String): HangulString = new HangulString(self)

  @inline implicit def string2Charset(charset: String): Charset = Charset.forName(charset)

  @inline implicit def fn2FilenameFilter(fn: (File, String) => Boolean): FilenameFilter = new FilenameFilter {
    def accept(file: File, name: String) = fn(file, name)
  }

}
