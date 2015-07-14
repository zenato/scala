package io.teamscala.scala.util

import org.scalatest._

class TraversableOpsSpec extends FlatSpec with Matchers {

  case class Person(name: String, age: Int)

  val people = Seq(
    Person("tester1", 18),
    Person("tester2", 18),
    Person("tester3", 19),
    Person("tester4", 19),
    Person("tester5", 20),
    Person("tester6", 21),
    Person("tester7", 18),
    Person("tester8", 22),
    Person("tester9", 23)
  )

  "#groupByOrdered" should "test groupByOrdered" in {
    val countsByAge = people.groupByOrdered(_.age).map { case (age, peopleByAge) => age -> peopleByAge.size }
    countsByAge.toSeq shouldBe Seq(18 -> 3, 19 -> 2, 20 -> 1, 21 -> 1, 22 -> 1, 23 -> 1)
  }

  "#groupConsecutiveKeys" should "test groupConsecutiveKeys" in {
    val countsByAge = people.groupConsecutiveKeys(_.age).map { case (age, peopleByAge) => age -> peopleByAge.size }
    countsByAge.toSeq shouldBe Seq(18 -> 2, 19 -> 2, 20 -> 1, 21 -> 1, 18 -> 1, 22 -> 1, 23 -> 1)
  }

  "#simple" should "test simple" in {
    people.map(_.name).simple(" 외 ", "건", "") shouldBe "tester1 외 8건"
    List.empty[String].simple(" 외 ", "건", "디폴트") shouldBe "디폴트"
  }
}
