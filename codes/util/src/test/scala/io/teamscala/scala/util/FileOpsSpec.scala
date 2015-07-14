package io.teamscala.scala.util

import java.io.File

import org.scalatest._

import scalax.io.JavaConverters._
import scalax.io._

class FileOpsSpec extends FlatSpec with Matchers {

  "#nonComflictFile" should "중복되지않는 파일 생성" in {
    val tmpDir = new File(System.getProperty("java.io.tmpdir"))
    val tmpFile = tmpDir.nonComflictFile
    tmpFile.exists shouldBe false
    tmpFile.asBinaryWriteChars(Codec.UTF8).write("This is temp file.")
    tmpFile.nonComflictFile.exists shouldBe false

    val testFile = new File(tmpDir, "test.txt").nonComflictFile
    testFile.exists shouldBe false
    testFile.asBinaryWriteChars(Codec.UTF8).write("This is test file.")
    testFile.nonComflictFile.exists shouldBe false
  }
}
