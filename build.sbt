import sbt.Keys.libraryDependencies

ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "flink-case"
version := "0.1-SNAPSHOT"
organization := "org.bitbucket.veysiertekin.flinkcase"

ThisBuild / scalaVersion := "2.11.12"

val flinkVersion = "1.6.2"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "compile",
  "org.apache.flink" %% "flink-table" % flinkVersion % "compile",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "compile")

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies,
    libraryDependencies += "org.rogach" % "scallop_2.11" % "3.1.4"
  )

assembly / mainClass := Some("org.bitbucket.veysiertekin.flinkcase.AnalyseTextFile")

// make run command include the provided dependencies
Compile / run := Defaults.runTask(Compile / fullClasspath,
  Compile / run / mainClass,
  Compile / run / runner
).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)
