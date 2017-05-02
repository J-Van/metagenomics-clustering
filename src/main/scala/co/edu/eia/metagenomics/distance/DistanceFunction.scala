package co.edu.eia.metagenomics.distance

import breeze.linalg.DenseVector
import de.lmu.ifi.dbs.elki.data.NumberVector
import de.lmu.ifi.dbs.elki.distance.distancefunction.AbstractNumberVectorDistanceFunction

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
//  override def distance(o1: NumberVector, o2: NumberVector): Double
}
