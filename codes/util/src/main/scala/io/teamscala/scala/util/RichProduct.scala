package io.teamscala.scala.util

final class RichProduct(val self: Product) extends AnyVal {

  @inline def isDefinedAny: Boolean = self.productIterator.exists {
    case null | None => false
    case _           => true
  }

  @inline def isEmptyAll: Boolean = !isDefinedAny

}
