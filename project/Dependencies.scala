import sbt._

object Dependencies {
  val util = Seq(
    Library.scalaAsync,
    Library.scalaParserCombinators,
    Library.scalaXml,
    Library.scalatest % Test,
    Library.scalamock % Test,
    Library.logbackClassic,
    Library.scalaIOFile,
    Library.nscalaTime,
    Library.commonsIO,
    Library.commonsCodec,
    Library.commonsLang3,
    Library.htmlCleaner
  )

  val resolvers = DefaultOptions.resolvers(snapshot = true) ++ Seq(
    "teamscala-nexus-repository" at "http://repo.teamscala.io/content/groups/public/"
  )
}

object Version {
  val scalaAsync             = "0.9.3"
  val scalaParserCombinators = "1.0.4"
  val scalaXml               = "1.0.4"

  val scalatest = "2.2.5"
  val scalamock = "3.2.2"

  val logbackClassic = "1.1.3"
  val scalaIOFile    = "0.4.3-1"
  val nscalaTime     = "2.0.0"
  val commonsIO      = "2.4"
  val commonsCodec   = "1.10"
  val commonsLang3   = "3.4"
  val htmlCleaner    = "2.12"
}

object Library {
  val scalaAsync             = "org.scala-lang.modules" %% "scala-async" % Version.scalaAsync
  val scalaParserCombinators = "org.scala-lang.modules" %% "scala-parser-combinators" % Version.scalaParserCombinators
  val scalaXml               = "org.scala-lang.modules" %% "scala-xml" % Version.scalaXml

  val scalatest = "org.scalatest" %% "scalatest" % Version.scalatest
  val scalamock = "org.scalamock" %% "scalamock-scalatest-support" % Version.scalamock

  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logbackClassic
  val scalaIOFile    = "com.github.scala-incubator.io" %% "scala-io-file" % Version.scalaIOFile
  val nscalaTime     = "com.github.nscala-time" %% "nscala-time" % Version.nscalaTime
  val commonsIO      = "commons-io" % "commons-io" % Version.commonsIO
  val commonsCodec   = "commons-codec" % "commons-codec" % Version.commonsCodec
  val commonsLang3   = "org.apache.commons" % "commons-lang3" % Version.commonsLang3
  val htmlCleaner    = "net.sourceforge.htmlcleaner" % "htmlcleaner" % Version.htmlCleaner
}
