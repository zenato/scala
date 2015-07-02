package io.teamscala.scala.util

import java.io.File
import java.nio.file.Files

import org.apache.commons.io.FilenameUtils.{getBaseName, getExtension}
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

import scala.util.matching.Regex.Groups

object RichFile {
  final val RegexNumberedBaseName = """(.*)\((\d+)\)$""".r
}

final class RichFile(val self: File) extends AnyVal {
  import RichFile._

  def nonComflictFile: File = {
    var path = if (self.isDirectory) self.toPath.resolve(randomAlphanumeric(8)) else self.toPath
    if (Files.exists(path)) {
      val (basename, beginCount, suffix) = {
        val fileName = path.getFileName.toString
        val (maybeNumberedBasename, suffix) = Option(getBaseName(fileName)).filter(_.nonEmpty) match {
          case Some(name) => name -> Option(getExtension(fileName)).filter(_.nonEmpty).map("." + _).getOrElse("")
          case None => fileName -> ""
        }
        RegexNumberedBaseName.findFirstMatchIn(maybeNumberedBasename).map {
          case Groups(name, cnt) => (name, cnt.toInt, suffix)
        }.getOrElse((maybeNumberedBasename, 1, suffix))
      }
      var count = beginCount
      do {
        path = path.resolveSibling(s"$basename($count)$suffix")
        count += 1
      } while (Files.exists(path))
    }
    path.toFile
  }

}
