package io.teamscala.scala.util

import org.scalatest._

class ProductOpsSpec extends FlatSpec with Matchers {

  "#isDefinedAny" should "어느 하나라도 None 또는 null 이 아닌지 확인" in {
    (None, None, null, Some(1)).isDefinedAny shouldBe true
    (None, None, null, Seq(1)).isDefinedAny shouldBe true
    (None, None, null, 1).isDefinedAny shouldBe true
    (None, None, null, Nil).isDefinedAny shouldBe false
  }

  "#isEmptyAll" should "모두 None 또는 null 인지 확인" in {
    (None, None, null, Some(1)).isEmptyAll shouldBe false
    (None, None, null, Seq(1)).isEmptyAll shouldBe false
    (None, None, null, 1).isEmptyAll shouldBe false
    (None, None, null, Nil).isEmptyAll shouldBe true
  }

}
