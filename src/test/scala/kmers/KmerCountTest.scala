package kmers

import co.edu.eia.metagenomics.kmers.KmerCount
import org.scalatest.FunSpec

import scala.collection.mutable
import scala.io.Source

class KmerCountTest extends FunSpec {

  describe("KMerCount") {
    it("should return HashMap(AAAA -> 1) for the text AAAA with k = 4") {
      assertResult(mutable.HashMap("AAAA" -> 1)) {
        KmerCount(4, "AAAA")
      }
    }

    it("should return HashMap(AAAA -> 3) for the text AAAAAA with k = 4") {
      assertResult(mutable.HashMap("AAAA" -> 3)) {
        KmerCount(4, "AAAAAA")
      }
    }

    it("should return HashMap(AAAA -> 2, AAAB -> 1) for the text AAAAAB") {
      assertResult(mutable.HashMap("AAAA" -> 2, "AAAB" -> 1)) {
        KmerCount(4, "AAAAAB")
      }
    }

    it("should return the appropriate number of 4mers in a text") {
      val file = Source.fromURL(getClass.getResource("/test.txt")).mkString
      assertResult(file.length - 4 + 1) {
        KmerCount(4, file).foldLeft(0)(_+_._2)
      }
    }
  }

}
