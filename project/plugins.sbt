addSbtPlugin("org.foundweekends.giter8" %% "sbt-giter8" % "0.12.0-M1")
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
