package co.edu.eia.metagenomics.distance

import breeze.linalg.{DenseVector, squaredDistance}

/**
 * Wrapper around Breeze's own implementation of the squared Euclidean distance, extending the DistanceFunction
 * trait so it can be referenced as a sub-type of it.
 */
object SquaredEuclideanDistance extends DistanceFunction {
  override def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double = {
    squaredDistance(q, p)
  }
}





