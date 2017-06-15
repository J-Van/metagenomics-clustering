package co.edu.eia.metagenomics

import java.io.File

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import scopt.OptionParser

case class Config(input: String = null,
                  output: String = null,
                  runs: Int = 3,
                  numClusters: Int = 3,
                  numIterations: Int = 200)

object ClusteringApp {

  def main(args: Array[String]): Unit = {
    val parser = new OptionParser[Config]("metagenomics") {
      head("metagenomics-clustering", "0.1")
      arg[String]("input").required()
        .action((x, c) => c.copy(input = x))
        .text("Path to the input file")
      arg[String]("output").required()
        .action((x, c) => c.copy(output = x))
        .text("Path where the output will be saved")
      opt[Int]('r', "runs").optional()
        .action((x, c) => c.copy(runs = x))
        .validate( x =>
          if (x > 1) success
          else failure("runs must be >1"))
        .text("Number of runs the clustering algorithm will be executed. Default is 3")
      opt[Int]('k', "clusters").optional()
        .action((x, c) => c.copy(numIterations = x))
        .validate( x =>
          if (x > 1) success
          else failure("Number of clusters must be >1"))
        .text("Number of clusters. Default is 3")
      opt[Int]('n', "iterations").optional()
        .action((x, c) => c.copy(numClusters = x))
        .validate( x =>
          if (x > 1) success
          else failure("Max Iterations must be >1"))
        .text("Max number of iterations that will be executed each run of the algorithm. Default is 200")
    }
    // TODO: This fails if the winutils.exe is inside the project (lib/hadoop/bin). How to handle distribution of this file?
    // This path needs to contain a 'bin' folder with winutils.exe inside, so it can run on Windows
    if (System.getProperty("os.name").toLowerCase.contains("win")) {
      System.setProperty("hadoop.home.dir", "C:\\Program Files\\hadoop")
    }
    parser.parse(args, Config()) match {
      case Some(config) =>
        val conf = new SparkConf().setAppName("KMeansMetagenomics")
        val sc = new SparkContext(conf)

        val data = sc.textFile(config.input)
        val parsedData = data.map(s => Vectors.dense(s.split(',').map(_.toDouble))).cache()

        val numClusters = config.numClusters
        val numIterations = config.numIterations
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
      case None =>
        // arguments are bad, usage will be displayed
    }

  }
}