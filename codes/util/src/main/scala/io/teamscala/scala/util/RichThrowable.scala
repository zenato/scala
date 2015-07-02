package io.teamscala.scala.util

import java.io.{PrintWriter, StringWriter}

final class RichThrowable(val self: Throwable) extends AnyVal {

  def stackTraceString: String = {
    val stackTrace = new StringWriter
    self.printStackTrace(new PrintWriter(stackTrace))
    stackTrace.toString
  }

}
