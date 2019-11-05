// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.3")

// Defines scaffolding (found under .g8 folder)
// http://www.foundweekends.org/giter8/scaffolding.html
// sbt "g8Scaffold form"
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")

// Less compilation
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")

// Eclipse Plugin
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.2")