
name := "ogu-ferrilo"

version := "0.2.5"

scalaVersion := "2.12.8"

resolvers += "clojars" at "https://clojars.org/repo"

libraryDependencies += "joda-time" % "joda-time" % "2.10.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "com.github.lalyos" % "jfiglet" % "0.0.8"
libraryDependencies += "org.clojure" % "clojure" % "1.10.0"
libraryDependencies += "org.clojure" % "data.json" % "1.1.0"

scalacOptions += "-deprecation"
