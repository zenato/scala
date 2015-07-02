package io.teamscala.scala.util

object HangulString {
  final val Numbers      = Array("영", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구")
  final val LargeNumbers = Array(Array("십", "백", "천"), Array("만", "억", "조", "경"))
}

final class HangulString(val self: String) extends AnyVal {
  import HangulString._

  @inline def isHangul: Boolean = self.nonEmpty && self.forall(_.isHangul)

  def toHangulNumber(delimiter: String = ""): String = if (!self.isDigits) self
  else {
    val numbers = self.dropWhile(_ == '0').map(_ - '0')
    if (numbers.isEmpty) return Numbers(0)
    if (numbers.length == 1) return Numbers(numbers.head)
    numbers.reverseIterator.grouped(4).zipWithIndex.map { case (nums, i) =>
      val hnums = nums.zipWithIndex.map { case (n, j) =>
        (if (n > 1 || j == 0 && n == 1) Numbers(n) else "") +
        (if (n > 0 && j > 0) LargeNumbers(0)(j - 1) else "")
      }.reduceRight { (l, r) => r + l }
      val lunit = if (i > 0 && hnums.nonEmpty) LargeNumbers(1)((i - 1) % 4) else ""
      hnums + lunit
    }.filter(_.nonEmpty).reduceRight { (l, r) => r + delimiter + l }
  }

  @inline def toHangulNumber: String = toHangulNumber()

}
