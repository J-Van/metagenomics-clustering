package co.edu.eia.metagenomics.distance

import breeze.linalg.DenseVector
import breeze.linalg.functions.{cosineDistance => breezeCosineDistance}

/**
 * Wrapper around Breeze's own implementation of the cosine distance, extending the DistanceFunction
 * trait so it can be referenced as a sub-type of it.
 */
object CosineDistance extends DistanceFunction {
  override def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double = {
    breezeCosineDistance(q, p)
  }
}
