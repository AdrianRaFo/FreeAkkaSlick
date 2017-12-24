lazy val freesV = "0.5.0"

val circeV = "0.9.0-M2"

val slickV = "3.2.1"

val akkaV = "2.4.20"

lazy val baseDeps: Seq[Def.Setting[_]] = Seq(
  scalaVersion := "2.12.4",
  wartremoverWarnings in (Compile, compile) ++= Warts.unsafe,
  scalacOptions ++= Seq(
    "-Ywarn-dead-code",
    "-Ypartial-unification",
    "-Ywarn-unused-import",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding",
    "UTF-8"
  ),
  cancelable := true,
  exportJars := true
)

lazy val freeDeps = Seq(
  "io.frees" %% "frees-core"    % freesV,
  "io.frees" %% "frees-akka"    % freesV,
  "io.frees" %% "frees-slick"   % freesV,
  "io.frees" %% "frees-logging" % freesV)

lazy val circeDeps =Seq(
  "io.circe" %% "circe-core"    % circeV,
  "io.circe" %% "circe-generic" % circeV,
  "io.circe" %% "circe-parser"  % circeV)

lazy val persistenceDeps = Seq(
  "com.typesafe.slick" %% "slick"           % slickV,
  "com.typesafe.slick" %% "slick-hikaricp"  % slickV,
  "com.typesafe.slick" %% "slick-codegen"   % slickV,
  "org.postgresql"     % "postgresql"       % "42.1.4")

lazy val akkaDeps = Seq(
  "com.typesafe.akka"  %% "akka-http"       % "10.0.11",
  "com.typesafe.akka"  %% "akka-actor"      % akkaV,
  "com.typesafe.akka"  %% "akka-slf4j"      % akkaV,
  "ch.qos.logback"     % "logback-classic"  % "1.2.3")

lazy val `FreeAkkaSlick` = project
  .in(file("."))
  .settings(name := "FreeAkkaSlick")
  .settings(moduleName := "FreeAkkaSlick")
  .settings(libraryDependencies++=freeDeps++circeDeps++persistenceDeps++akkaDeps)
  .settings(slickGen := slickCodeGenTask.value) // register manual sbt command
  .settings(
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  scalacOptions ++= Seq("-Xplugin-require:macroparadise","-Ywarn-unused-import"),
  scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise"))
  )

lazy val slickGen = TaskKey[Seq[File]]("slickGen")

lazy val slickCodeGenTask = Def.task {
  val outputDir = "src/main/scala/adrianrafo"
  (runner in Compile).value.run(
    "slick.codegen.SourceCodeGenerator",
    (dependencyClasspath in Compile).value.files,
    Array(
      "slick.jdbc.PostgresProfile",
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost/test?currentSchema=public",
      outputDir,
      "gen",
      "test",
      "test"
    ),
    streams.value.log
  )
  Seq(file(s"$outputDir/gen/Tables.scala"))
}