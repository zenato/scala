package io.teamscala.scala.util

import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.{Date, Locale, TimeZone}

import org.apache.commons.codec.binary.Hex
import org.apache.commons.lang3.StringUtils
import org.htmlcleaner._

import scala.util.matching.Regex
import scala.util.matching.Regex._

object StringOps {
  final val RegexSnakeCase = """[-_ ]([\da-z])""".r
  final val RegexDuplicatedSlash = """/{2,}""".r
  final val RegexAntStylePatternSpecialCharacters = """[-\[\]{}()+.,\\^$|#\s]""".r
  final val RegexAntStylePatternWildcardsWithSlash = """(/?\*\*)|(/?\*)|(\?)|(/)""".r
  final val RegexBeginningOfEachLine = """(?m)^""".r
  final val RegexAllTags = """<[/\!]*?[^<>]*?>""".r
  final val RegexTagAttributes = """(\S+)=["']?((?:.(?!["']?\s+(?:\S+)=|[>"']))+.)["']?""".r

  def defaultHtmlCleanerProperties: CleanerProperties = {
    val props = new CleanerProperties()
    props.setAllowHtmlInsideAttributes(true)
    props.setRecognizeUnicodeChars(false)
    props.setOmitXmlDeclaration(true)
    props
  }
}

final class StringOps(val self: String) extends AnyVal {
  import StringOps._

  @inline def isDigits: Boolean = self.nonEmpty && self.forall(_.isDigit)

  @inline def uncapitalize: String = StringUtils.uncapitalize(self)

  @inline def toCamelCase: String = RegexSnakeCase.replaceAllIn(self.toLowerCase, _.group(1).toUpperCase)

  def toAntStylePattern: Regex = {
    val cleanedPath = RegexDuplicatedSlash.replaceAllIn(self, "/")
    val escapedPath = RegexAntStylePatternSpecialCharacters.replaceAllIn(cleanedPath, "\\\\$0")
    val pattern = RegexAntStylePatternWildcardsWithSlash.replaceAllIn(escapedPath, {
      _.group(0) match {
        case "/**" => "(?:/{1,}.*)?"
        case "**"  => ".*"
        case "/*"  => "(?:/{1,}[^/]*)?"
        case "*"   => "[^/]*"
        case "?"   => "[^/]"
        case "/"   => "/{1,}"
      }
    })
    ("^(" + pattern + ")$").r
  }

  /** Shortcut for [[io.teamscala.scala.util.StringOps#toAntStylePattern]] */
  @inline def a: Regex = toAntStylePattern

  @inline def prependEachLine(prefix: String): String = RegexBeginningOfEachLine.replaceAllIn(self, prefix)

  @inline def stripTags: String = RegexAllTags.replaceAllIn(self, "")

  def cutstring(length: Int, suffix: String = ""): String =
    if (self.length <= length - suffix.length) self
    else self.substring(0, length - suffix.length) + suffix

  def parseDate[T <: String](
    pattern:  T,
    timeZone: TimeZone = TimeZone.getDefault,
    locale:   Locale   = Locale.getDefault
  ): Date = {
    val parser = new SimpleDateFormat(pattern, locale)
    parser.setLenient(true)
    parser.setTimeZone(timeZone)
    parser.parse(self)
  }

  def parseMap(trimValue: Boolean = false): Map[String, String] = if (self.isEmpty) Map.empty
  else {
    val isScalaMap = self.startsWith("Map(")
    val headOffset = if (isScalaMap) 4 else if (self.head == '{') 1 else 0
    val lastOffset = if (")}" contains self.last) 1 else 0
    val mapString = self.slice(headOffset, self.length - lastOffset)
    val mapEq = if (isScalaMap) " -> " else "="

    mapString.split(", ").map { pair =>
      try {
        val Array(key, value) = pair.split(mapEq, 2)
        key -> (if (trimValue) value.trim else value)
      } catch {
        case _: MatchError => throw new IllegalArgumentException("No " + mapEq + " after " + pair)
      }
    }.toMap
  }

  @inline def parseMap: Map[String, String] = parseMap()

  def checkTranscoding(encoding: Charset): Set[String] = self.foldLeft(Set.empty[String]) { (z, c) =>
    val s = String.valueOf(c)
    val convertedChar = new String(new String(s.getBytes(encoding), encoding).getBytes)
    if (s != convertedChar) z + s else z
  }

  def encrypt(algorithm: String): String = {
    val md = MessageDigest.getInstance(algorithm)
    md.update(self.getBytes)
    new String(Hex encodeHex md.digest)
  }
  @inline def encryptMD5: String = encrypt("MD5")
  @inline def encryptSHA: String = encrypt("SHA")
  @inline def encryptSHA256: String = encrypt("SHA-256")
  @inline def encryptSHA384: String = encrypt("SHA-384")
  @inline def encryptSHA512: String = encrypt("SHA-512")

  @inline def nl2br: String = StringUtils.replaceEach(self, Array("\r\n", "\n"), Array("<br/>", "<br/>"))
  @inline def br2nl: String = StringUtils.replaceEach(self, Array("<br/>", "<br>"), Array("\n", "\n"))
  @inline def space2nbsp: String = StringUtils.replace(self, " ", "&nbsp;")
  @inline def nbsp2space: String = StringUtils.replace(self, "&nbsp;", " ")

  def htmlArrange(
    parentTagName: String                              = "html",
    innerHtml:     Boolean                             = false,
    props:         CleanerProperties                   = defaultHtmlCleanerProperties,
    serializer:    CleanerProperties => HtmlSerializer = new SimpleHtmlSerializer(_)
  ): String = {
    val cleaner = new HtmlCleaner(props)
    val ser = serializer(props)
    val rootNode = cleaner.clean(self)
    val innerNode = if (parentTagName.toLowerCase == "html") rootNode
    else rootNode.findElementByName(parentTagName, true)
    if (innerNode == null) ""
    else {
      val html = ser.getAsString(innerNode)
      if (innerHtml) {
        val begin = html.indexOf('>', html.indexOf("<" + innerNode.getName) + 1)
        val end = html.lastIndexOf('<')
        if (begin >= 0 && begin <= end) html.substring(begin + 1, end) else ""
      } else html
    }
  }

  def htmlCleanXSS(
    parentTagName: String                              = "html",
    innerHtml:     Boolean                             = false,
    props:         CleanerProperties                   = defaultHtmlCleanerProperties,
    serializer:    CleanerProperties => HtmlSerializer = new SimpleHtmlSerializer(_)
  ): String = {
    props.setPruneTags("script,style,iframe")
    RegexTagAttributes.replaceAllIn(htmlArrange(parentTagName, innerHtml, props, serializer), {
      // Avoid anything in a src='...' type of expression
      case Groups("src", _) => ""
      // Avoid on...= expressions
      case Groups(key, _) if key.startsWith("on") => ""
      // Avoid eval(...) expressions
      case Groups(_, value) if "(?i)eval\\((.*?)\\)".r.findFirstIn(value).isDefined => ""
      // Avoid expression(...) expressions
      case Groups(_, value) if "(?i)expression\\((.*?)\\)".r.findFirstIn(value).isDefined => ""
      // Avoid javascript:... expressions
      case Groups(_, value) if "(?i)javascript:".r.findFirstIn(value).isDefined => ""
      // Avoid vbscript:... expressions
      case Groups(_, value) if "(?i)vbscript:".r.findFirstIn(value).isDefined => ""
      case Match(m) => m
    }: Match => String)
  }
}
