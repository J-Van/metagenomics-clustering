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
}
