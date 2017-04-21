package co.edu.eia.metagenomics.distance

import breeze.linalg.{DenseVector, sum}
import breeze.numerics.log

/**
 * Implementation of the Jensen-Shannon dissimilarity applied to two vectors, see "Comprehensive Survey on Distance/Similarity
 * Measures between Probability Density Functions" by Sung-Hyuk Cha at http://users.uom.gr/~kouiruki/sung.pdf
 */
object JensenShannonDistance extends DistanceFunction {
  override def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double = {
    def auxFunction(x: DenseVector[Double], y: DenseVector[Double]): Double = {
      sum(x +:+ log((x :*= 2.0) / (x + y)))
    }
    (1 / 2) * (auxFunction(q, p) + auxFunction(p, q))
  }
}
