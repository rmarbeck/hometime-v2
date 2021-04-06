name := """hometime-v2"""
organization := "fr.hometime"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  javaWs,
  "org.webjars" % "bootstrap" % "4.3.1"
)

includeFilter in (Assets, LessKeys.less) := "merged.less" | "style.less" | "responsive-proxy.less" | "dark-proxy.less" | "custom.less"

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes

herokuAppName in Compile := "hometime-v2"

pipelineStages := Seq(digest, gzip)


