package co.edu.eia.metagenomics.utils

import org.apache.spark.mllib.linalg.{Vector, Vectors}

object PointWithCategory {
  def apply(data: String): (Vector, String) = {
    val dataArray = data.split(',')
    val category = dataArray.last
    val point = dataArray.dropRight(1).map(_.toDouble)
    Vectors.dense(point) -> category
  }
}
