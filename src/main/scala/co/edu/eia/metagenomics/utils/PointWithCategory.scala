package co.edu.eia.metagenomics.utils

import org.apache.spark.mllib.linalg.{Vector, Vectors}

object PointWithCategory {
  def apply(data: String): (Vector, Option[String]) = {
    val dataArray = data.split(',')
    val category = dataArray.last
    val point = dataArray.dropRight(1).map(_.toDouble)
    if (!category.isEmpty) Vectors.dense(point) -> Some(category)
    else Vectors.dense(point) -> None
  }
}
