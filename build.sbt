import xerial.sbt.Sonatype.sonatypeCentralHost

name := """sbt-resolver-credentials"""
//organization := "com.orzitzer"
//version := "0.1-SNAPSHOT"

sbtPlugin := true

// choose a test framework

// utest
//libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.10" % "test"
//testFrameworks += new TestFramework("utest.runner.Framework")

// ScalaTest
//libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9" % "test"
//libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

// Specs2
//libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.12.8" % "test")
//scalacOptions in Test ++= Seq("-Yrangepos")

inThisBuild(List(
  organization := "com.orzitzer",
  homepage := Some(url("https://github.com/e-orz/sbt-resolver-credentials")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "e-orz",
      "Eli Orzitzer",
      "e_orz@yahoo.com",
      url("https://github.com/e-orz")
    )
  )
))

console / initialCommands := """import com.orzitzer.sbt._"""

enablePlugins(ScriptedPlugin)
// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")))

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)

ThisBuild / sonatypeCredentialHost := sonatypeCentralHost
ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))
// Publishing to maven central requires the below config. see https://www.scala-sbt.org/1.x/docs/Plugins.html#Publishing+a+plugin
ThisBuild / sbtPluginPublishLegacyMavenStyle := false
