package co.edu.eia.metagenomics.distance

import breeze.linalg.{DenseVector, sum}
import breeze.numerics.pow

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
