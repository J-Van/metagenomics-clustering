package co.edu.eia.metagenomics

import co.edu.eia.metagenomics.utils.PointWithCategory
import org.apache.spark.mllib.clustering.{GaussianMixture, KMeans}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import scopt.OptionParser

case class Config(input: String = null,
                  output: String = null,
                  algorithm: String = "kmeans",
                  iterative: Boolean = false,
                  fraction: Double = 0.75,
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
      opt[Unit]('i', "iterative").optional()
        .action((_, c) => c.copy(iterative = true))
        .text("Flag specifying if the next iteration should use the results of the previous one (Iterative Clustering)")
      opt[Double]('f', "fraction").optional()
        .action((x, c) => c.copy(fraction = x))
        .text("Optional fraction of the sample to take in non-iterative clustering. Default is 0.75")
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
      opt[Int]('n', "maxiter").optional()
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
          data.filter(s => !s.startsWith("@")).map(s => PointWithCategory(s)).cache()
        else
          data.filter(s => !s.startsWith("@")).map(s => Vectors.dense(s.split(',').map(_.toDouble)) -> None[String]).cache()

        var centers: Array[Vector] = Array(Vectors.zeros(1))

        val numClusters = config.numClusters
        val numIterations = config.numIterations

        val output = if (!config.iterative) {
          val models = (1 to config.runs).map(_ =>
              new GaussianMixture().setK(numClusters)
                .setMaxIterations(numIterations)
                .run(parsedData.map(_._1).sample(withReplacement = false, fraction = 0.75)))
          val result = parsedData.map(pointAndCenter =>
            (pointAndCenter._1, models.map(_.predict(pointAndCenter._1)), pointAndCenter._2))
          result
        } else {
          for (i <- 1 to config.runs) {
            val pointsWithCenterAndDistance = if (config.algorithm == "kmeans") {
              val clusters = new KMeans().setK(numClusters)
                .setMaxIterations(numIterations)
                .setEpsilon(1e-2)
                .setInitializationMode("kmeans||")
                .setSeed(10)
                .run(parsedData.map(_._1))
              centers = clusters.clusterCenters
              for {
                point <- parsedData.map(_._1)
                center = centers(clusters.predict(point))
                distance = Vectors.sqdist(point, center)
              } yield point -> (center, distance)
            } else {
              val clusters = new GaussianMixture().setK(numClusters)
                .setMaxIterations(numIterations)
                .run(parsedData.map(_._1))
              centers = clusters.gaussians.map(_.mu)
              for {
                point <- parsedData.map(_._1)
                center = centers(clusters.predict(point))
                distance = Vectors.sqdist(point, center)
              } yield point -> (center, distance)
            }
            val distances = for {
              i <- 0 until centers.length - 1
              j <- i+1 until centers.length
            } yield Vectors.sqdist(centers(i), centers(j))
            val averageDistanceCenters = distances.sum/distances.length
            val standardDeviation = Math.sqrt(distances.map(distance => Math.pow(Math.abs(distance - averageDistanceCenters), 2)).sum/distances.length)
            val margin = averageDistanceCenters + standardDeviation
            "a"
          }
        }
        output.asInstanceOf[RDD].saveAsTextFile(config.output)
        sc.stop()
      case None =>
      // arguments are bad, usage will be displayed
    }
  }

  def generateBalancedSample(data: RDD[(Vector, Option[String])], fraction: Double): RDD[Vector] = {
    //val sample = data.groupBy(_._2.getOrElse("")).zip(RandomRDDs.randomRDD())
    ???
  }
}