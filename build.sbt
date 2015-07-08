lazy val `scala-root` = (project in file(".")).settings(
  publishArtifact := false,
  publish := (),
  bintrayRelease := (),
  bintrayUnpublish := ()
).aggregate(
  `scala-util`
)

lazy val `scala-util` = (project in file("codes/util")).settings(
  libraryDependencies ++= Dependencies.util ++ Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)
