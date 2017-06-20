name := "MetagenomicsClustering"

version := "0.1"

scalaVersion := "2.11.10"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.1.0"
libraryDependencies += "org.scalanlp" %% "breeze-natives" % "0.13"
libraryDependencies += "de.lmu.ifi.dbs.elki" % "elki" % "0.7.1"
libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.6.0"

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

parallelExecution in Test := false