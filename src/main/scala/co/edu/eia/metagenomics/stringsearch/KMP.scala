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
    require(text.length > pattern.length, "Text must be larger than the pattern")
    var count = 0
    var m = 0
    var i = 0
    val partialMatchTable: Array[Int] = createTable(pattern)
    while (m + i < text.length) {
      if (pattern.charAt(i) == text.charAt(m + i)) {
        if (i == pattern.length - 1) {
          count += 1
          m += 1
          i = 0
        } else {
          i += 1
        }
      } else {
        if (partialMatchTable(i) > -1) {
          m = m + i - partialMatchTable(i)
          i = partialMatchTable(i)
        } else {
          m += 1
          i = 0
        }
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
    if (pattern.length == 1) Array(-1)
    var i = 0
    var j = 2
    val table = Array.fill(pattern.length){0}
    table(0) = -1
    while (j < pattern.length) {
      if (pattern(j - 1) == pattern(i)) {
        table(j) = i + 1
        i += 1
        j += 1
      } else if (i > 0) {
        i = table(i)
      } else {
        table(j) = 0
        j += 1
      }
    }
    table
  }

}
