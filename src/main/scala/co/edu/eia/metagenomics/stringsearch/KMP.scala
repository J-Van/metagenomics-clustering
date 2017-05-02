package co.edu.eia.metagenomics.stringsearch

/**
  * Variation of the Knuth-Morris-Pratt algorithm to count occurrences of a pattern in a text
  */
object KMP {
  /**
    * Counts the number of occurrences of the pattern in the text that is being searched
    * @param pattern  Pattern that will be searched in the text
    * @param text     Text to be searched
    * @return         Int with the number of occurrences of the pattern in the text
    */
  def count(pattern: String, text: String): Int = {
    require(pattern.length > 0, "Pattern must have at least one character")
    require(text.length > pattern.length, "Text must be larger than the pattern to search for it")
    var count = 0
    var m = 0
    var i = 0
    val partialMatchTable: Array[Int] = createTable(pattern)
    while (m + i < text.length) {
      if (pattern.charAt(i) == text.charAt(m + i)) {
        count += (if (i == pattern.length - 1) 1 else 0)
        i += 1
      } else {
        val start = if (partialMatchTable(i) > -1) partialMatchTable(i) else 0
        m = m + i - start
        i = start
      }
    }
    count
  }

  /**
    * Create partial match table for the KMP algorithm
    * @param pattern  Pattern that is going to be searched in the KMP algorithm
    * @return         Partial Match Table for the pattern input
    */
  def createTable(pattern: String): Array[Int] = {
    def inner(i: Int, j: Int, table: Array[Int] = Array(-1, 0)): Array[Int] = {
      if (j == pattern.length - 1) table
      else {
        val isMatch = pattern(i) == pattern(j)
        val index = if (isMatch) table.last + 1 else 0
        inner(if (isMatch) i + 1 else i, j + 1, table :+ index)
      }
    }
    if (pattern.length == 1) Array(-1)
    else inner(0, 1)
  }

}
