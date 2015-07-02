package io.teamscala.scala.util

import scala.collection.generic.CanBuildFrom
import scala.collection.{TraversableLike, immutable, mutable}

final class RichTraversableLike[+A, Repr](val self: TraversableLike[A, Repr]) extends AnyVal {

  def groupByOrdered[K](f: A => K)(implicit cbf: CanBuildFrom[Repr, A, Repr]): immutable.ListMap[K, Repr] = {
    val m = mutable.LinkedHashMap.empty[K, mutable.Builder[A, Repr]]
    for (elem <- self) {
      val key = f(elem)
      val bldr = m.getOrElseUpdate(key, cbf())
      bldr += elem
    }
    val b = immutable.ListMap.newBuilder[K, Repr]
    for ((k, v) <- m) b += ((k, v.result()))
    b.result()
  }

  def groupConsecutiveKeys[K](f: A => K): Seq[(K, Seq[A])] =
    self.foldRight(List[(K, List[A])]()) { (item: A, res: List[(K, List[A])]) =>
      res match {
        case Nil                                => List((f(item), List(item)))
        case (k, klist) :: tail if k == f(item) => (k, item :: klist) :: tail
        case _                                  => (f(item), List(item)) :: res
      }
    }

  def simple(besides: String = "...(", postfix: String = ")", default: String = ""): String =
    self.headOption.map { head =>
      if (self.size == 1) head.toString
      else s"$head$besides${self.size - 1}$postfix"
    }.getOrElse(default)

  @inline def simple: String = simple()

}
