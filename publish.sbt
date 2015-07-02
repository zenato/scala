credentials in ThisBuild += Credentials(Path.userHome / ".ivy2" / ".credentials-teamscala")

publishTo in ThisBuild := {
  val nexus = "http://repo.teamscala.io/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "content/repositories/releases")
}

publishMavenStyle in ThisBuild := true

publishArtifact in (ThisBuild, Test) := false

pomIncludeRepository in ThisBuild := { _ => false}

pomExtra in ThisBuild := {
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
