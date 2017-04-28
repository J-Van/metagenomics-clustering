name := "MetagenomicsClustering"

version := "1.0"

scalaVersion := "2.11.10"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.1.0"
libraryDependencies += "org.scalanlp" %% "breeze-natives" % "0.13"
libraryDependencies += "de.lmu.ifi.dbs.elki" % "elki" % "0.7.1"