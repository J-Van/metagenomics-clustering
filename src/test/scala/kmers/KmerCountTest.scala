package kmers

import co.edu.eia.metagenomics.kmers.KmerCount
import org.scalatest.FunSpec

import scala.collection.mutable

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
  }

}
