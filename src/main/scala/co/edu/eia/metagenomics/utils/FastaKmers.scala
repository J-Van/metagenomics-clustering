package co.edu.eia.metagenomics.utils

import co.edu.eia.metagenomics.kmers.KmerCount

import scala.collection.immutable.VectorBuilder
import scala.collection.mutable

/** Auxiliary object to count the Kmers in each of the reads contained in the Fasta file */
//TODO: May run out of memory with big Fasta files (>1GB). Needs to be better
object FastaKmers {
  def apply(k: Int, source: Iterator[String]): Vector[(String, mutable.Map[String, Int])] = {
    val builder = new VectorBuilder[(String, mutable.Map[String, Int])]
    while (source.hasNext) {
      val name = source.next().stripPrefix(">")
      val count = KmerCount(k, source.next())
      builder += ((name, count))
      println(s"Passed $name")
    }
    builder.result()
  }
}
