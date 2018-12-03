import play.core.PlayVersion
import sbt._

object AppDependencies {
  val compile = Seq(
    "com.typesafe.play" %% "play"  % PlayVersion.current % "provided",
    "net.ceedubs"       %% "ficus" % "1.1.1"
  )

  val test = Seq(
    "com.typesafe.play" %% "play-test" % PlayVersion.current % Test,
    "org.scalatest"     %% "scalatest" % "2.2.4"             % Test,
    "org.pegdown"       % "pegdown"    % "1.5.0"             % Test
  )
}
