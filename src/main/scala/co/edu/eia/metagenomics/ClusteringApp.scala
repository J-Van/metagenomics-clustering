package co.edu.eia.metagenomics

import org.apache.spark.{SparkConf, SparkContext}

object ClusteringApp {
  def main(args: Array[String]) {
    val logFile = "test.txt"
    val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    sc.stop()
  }
}