import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(ScriptedPlugin)
  .settings(
    name := "tagless-final",
    organization  := "es.hablapps",
    scalaVersion  := "2.12.8",
    version       := "1.0.0-SNAPSHOT",
    Defaults.itSettings,
    scalacOptions += "-Ypartial-unification",
    scriptedLaunchOpts ++= List(
      "-Xms1024m",
      "-Xmx1024m",
      "-XX:ReservedCodeCacheSize=128m",
      "-Xss2m",
      "-Dfile.encoding=UTF-8"),
    resolvers += Resolver.url(
      "typesafe",
      url("http://repo.typesafe.com/typesafe/ivy-releases/")
    )(Resolver.ivyStylePatterns),
    libraryDependencies ++= Seq(
      catsCore,
      catsEffect,
      shapeless,
      specs2 % Test
    )
  )
