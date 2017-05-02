package co.edu.eia.metagenomics.kmers

import scala.collection.mutable

/** Auxiliary object to count the k-mers in a text. */
object KmerCount {
  /** Counts all the substrings of certain length in the given text
    *
    * @param k    Length of the substring that is going to be counted
    * @param text Text where the substrings will be counted
    * @return     HashMap with the substring as key and the count as value
    */
  def apply(k: Int, text: String): mutable.Map[String, Int] = {
    require(k > 1, "K must be greater than 1")
    require(k <= text.length, "K must be smaller or equal to the length of the text")
    val result = new mutable.HashMap[String, Int]().withDefaultValue(0)
    for (i <- k to text.length) {
      result(text.substring(i - k, i)) += 1
    }
    result
  }

  /** Returns a HashMap with the counts for each kmer normalized with the total of kmers in the text, being
    * the sum of all values in the Map.
    *
    * @param map    HashMap with the raw counts of each kmer
    * @param total  Total of kmers in the text
    * @return       HashMap with the values normalized based on the total
    */
  def normalizeKmers(map: mutable.HashMap[String, Int], total: Int): mutable.HashMap[String, Double] = {
    for ((k, v) <- map) yield (k, BigDecimal(v/total).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble)
  }
}
