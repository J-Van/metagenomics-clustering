package co.edu.eia.metagenomics.mllib

import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors

object KMeansExample {
  def main(args: Array[String]) {
    //TODO: This fails if the winutils.exe is inside the project (lib/hadoop/bin). How to handle this file?
    //This folder needs to contain a 'bin' folder with winutils.exe so it can run on Windows
    System.setProperty("hadoop.home.dir", "C:\\Program Files\\hadoop")
    val conf = new SparkConf().setAppName("KMeansExample").setMaster("local")
    val sc = new SparkContext(conf)

    val data = sc.textFile("iris.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(',').map(_.toDouble))).cache()

    val numClusters = 3
    val numIterations = 500
    val clusters = new KMeans().setK(numClusters)
                               .setMaxIterations(numIterations)
                               .setEpsilon(1e-2)
                               .setInitializationMode("kmeans||")
                               .setSeed(10)
                               .run(parsedData)

    val WSSSE = clusters.computeCost(parsedData)
    val centers = clusters.clusterCenters.deep.mkString(" ")
    println(s"Within Set Sum of Squared Errors = $WSSSE")
    println(s"Centers = $centers")
    //clusters.save(sc, "target/co/edu/eia/metagenomics/KMeansExample/KMeansModel")

    sc.stop()
  }
}
