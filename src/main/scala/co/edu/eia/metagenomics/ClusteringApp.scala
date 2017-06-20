package co.edu.eia.metagenomics

import co.edu.eia.metagenomics.utils.PointWithCategory
import org.apache.spark.mllib.clustering.{GaussianMixture, KMeans}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.{SparkConf, SparkContext}
import scopt.OptionParser

case class Config(input: String = null,
                  output: String = null,
                  algorithm: String = "kmeans",
                  runs: Int = 3,
                  numClusters: Int = 3,
                  numIterations: Int = 200,
                  classes: Boolean = false)

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
      opt[String]('a', "algorithm").optional()
        .action((x, c) => c.copy(algorithm = x))
        .validate( x =>
          if (x == "kmeans" | x == "gaussian") success
          else failure("Algorithm must be either 'kmeans' or 'gaussian'"))
        .text("Algorithm that will be used for the clustering. Options are 'kmeans' and 'gaussian'. Default is kmeans")
      opt[Int]('r', "runs").optional()
        .action((x, c) => c.copy(runs = x))
        .validate( x =>
          if (x > 1) success
          else failure("runs must be >1"))
        .text("Number of runs the clustering algorithm will be executed. Default is 3")
      opt[Int]('k', "clust").optional()
        .action((x, c) => c.copy(numIterations = x))
        .validate( x =>
          if (x > 1) success
          else failure("Number of clusters must be >1"))
        .text("Number of clusters. Default is 3")
      opt[Int]('n', "iter").optional()
        .action((x, c) => c.copy(numClusters = x))
        .validate( x =>
          if (x > 1) success
          else failure("Max Iterations must be >1"))
        .text("Max number of iterations that will be executed each run of the algorithm. Default is 200")
      opt[Unit]('c', "classes").optional()
        .action((_, c) => c.copy(classes = true))
        .text("Flag specifying if the data has the classes for comparison as the last attribute")
    }
    // TODO: This fails if the winutils.exe is inside the project (lib/hadoop/bin). How to handle distribution of this file?
    // This path needs to contain a 'bin' folder with winutils.exe inside, so it can run on Windows
    if (System.getProperty("os.name").toLowerCase.contains("win")) {
      System.setProperty("hadoop.home.dir", "C:\\Program Files\\hadoop")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        val conf = new SparkConf().setAppName("KMeansMetagenomics")
        conf.setIfMissing("spark.master", "local")
        val sc = new SparkContext(conf)

        val data = sc.textFile(config.input)
        val parsedData = if (config.classes)
          data.map(s => PointWithCategory(s)._1).cache()
        else
          data.map(s => Vectors.dense(s.split(',').map(_.toDouble))).cache()

        for ( i <- 1 to config.runs ) {
          val numClusters = config.numClusters
          val numIterations = config.numIterations
          val clusters = if (config.algorithm == "kmeans") {
            new KMeans().setK(numClusters)
              .setMaxIterations(numIterations)
              .setEpsilon(1e-2)
              .setInitializationMode("kmeans||")
              .setSeed(10)
              .run(parsedData)
          } else {
            new GaussianMixture().setK(numClusters)
              .setMaxIterations(numIterations)
              .run(parsedData)
          }
          //TODO GaussianMixture doesn't return centers
          val centers = clusters.clusterCenters
          val pointsWithCenterAndDistance = parsedData.map ( point => {
            var shortestDistance = Double.MaxValue
            var closestCenter = centers(0)
            for ( center <- centers ) {
              val distance = Vectors.sqdist(point, center)
              if (distance < shortestDistance) {
                shortestDistance = distance
                closestCenter = center
              }
            }
            point -> (closestCenter, shortestDistance)
          })
        }
        sc.stop()
      case None =>
        // arguments are bad, usage will be displayed
    }

  }
}