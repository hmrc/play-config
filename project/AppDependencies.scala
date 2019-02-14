import play.core.PlayVersion
import sbt._

object AppDependencies {
  val compile = Seq(
    "com.typesafe.play" %% "play"  % PlayVersion.current % "provided",
    "net.ceedubs"       %% "ficus" % "1.1.1",
    // force dependencies due to security flaws found in jackson-databind < 2.9.x using XRay
    "com.fasterxml.jackson.core"     % "jackson-core"            % "2.9.7",
    "com.fasterxml.jackson.core"     % "jackson-databind"        % "2.9.7",
    "com.fasterxml.jackson.core"     % "jackson-annotations"     % "2.9.7",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8"   % "2.9.7",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.9.7",
    // force dependencies due to security flaws found in xercesImpl 2.11.0
    "xerces" % "xercesImpl" % "2.12.0"
  )

  val test = Seq(
    "com.typesafe.play" %% "play-test" % PlayVersion.current % Test,
    "org.scalatest"     %% "scalatest" % "2.2.4"             % Test,
    "org.pegdown"       % "pegdown"    % "1.5.0"             % Test
  )
}
