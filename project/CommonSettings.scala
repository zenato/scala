import net.virtualvoid.sbt.graph.Plugin.graphSettings
import sbt.Keys._
import sbt._

object CommonSettings extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  override def projectSettings = Seq(
    organization := "io.teamscala.scala",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-deprecation",
      "-unchecked",
      "-feature",
      "-Xcheckinit",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:postfixOps"
    ),
    javacOptions in Compile ++= Seq("-encoding", "utf8", "-g"),
    resolvers ++= Dependencies.resolvers,
    parallelExecution in Test := false,
    fork in Test := true,
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    publishMavenStyle := true,
    pomIncludeRepository := { _ => false }
  ) ++ graphSettings
}
