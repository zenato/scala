import bintray.BintrayPlugin
import bintray.BintrayKeys._
import net.virtualvoid.sbt.graph.Plugin.graphSettings
import sbt.Keys._
import sbt._

object CommonSettings extends AutoPlugin {
  override def requires = BintrayPlugin
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
    fork in Test := true
  ) ++ publishSettings ++ graphSettings

  def publishSettings = Seq(
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials-teamscala"),
    publishTo := {
      if (isSnapshot.value)
        Some("teamscala-snapshots" at "http://repo.teamscala.io/content/repositories/snapshots")
      else
        (publishTo in bintray).value
    },
    bintrayReleaseOnPublish := !isSnapshot.value,
    publishMavenStyle := true,
    pomIncludeRepository := { _ => false },
    pomExtra := {
      <url>https://github.com/stonexx/scala</url>
      <scm>
        <url>git://github.com/stonexx/scala.git</url>
        <connection>scm:git:git://github.com/stonexx/scala.git</connection>
      </scm>
      <developers>
        <developer>
          <id>stonexx</id>
          <name>Seok Ki Won</name>
          <email>seok.kiwon@gmail.com</email>
        </developer>
        <developer>
          <id>zenato</id>
          <name>Lee Young Jin</name>
          <email>me.yjlee@gmail.com</email>
        </developer>
      </developers>
    }
  )
}
