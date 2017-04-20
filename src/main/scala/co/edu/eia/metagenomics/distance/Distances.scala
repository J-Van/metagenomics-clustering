package co.edu.eia.metagenomics.distance

import breeze.linalg.{DenseVector, squaredDistance, sum}
import breeze.linalg.functions.cosineDistance
import breeze.numerics.{log, pow}

/**
 * Trait that defines explicit implementations of distances between DenseVectors (from the Breeze library).
 * Defines signature for the calculateDistance function.
 */
trait DistanceFunction {
  /** Returns the distance between the two vectors.
   * @param q  First vector
   * @param p  Second vector
   * @return   Double with the distance between the two vectors
   */
  def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double
}

/**
 * Wrapper around Breeze's own implementation of the squared Euclidean distance, extending the DistanceFunction
 * trait so it can be referenced as a sub-type of it.
 */
object SquaredEuclideanDistance extends DistanceFunction {
  override def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double = {
    squaredDistance(q, p)
  }
}

/**
 * Implementation of the Jaccard dissimilarity applied to two vectors, see "Comprehensive Survey on Distance/Similarity
 * Measures between Probability Density Functions" by Sung-Hyuk Cha at http://users.uom.gr/~kouiruki/sung.pdf
 */
object JaccardDistance extends DistanceFunction {
  override def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double = {
    val sJac = sum(p *:* q) / (sum(pow(p, 2)) + sum(pow(q, 2)) - sum(p *:* q))
    val dJac = 1 - sJac
    dJac
  }
}

/**
 * Wrapper around Breeze's own implementation of the cosine distance, extending the DistanceFunction
 * trait so it can be referenced as a sub-type of it.
 */
object CosineDistance extends DistanceFunction {
  override def calculateDistance(q: DenseVector[Double], p: DenseVector[Double]): Double = {
    cosineDistance(q, p)
  }
}

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