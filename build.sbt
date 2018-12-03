val appName = "play-config"

lazy val library = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 7,
    makePublicallyAvailableOnBintray := true,
    scalaVersion := "2.11.12",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    crossScalaVersions := Seq("2.11.12"),
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.typesafeRepo("releases")
    )
  )
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
