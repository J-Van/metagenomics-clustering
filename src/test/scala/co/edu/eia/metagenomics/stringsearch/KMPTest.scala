package co.edu.eia.metagenomics.stringsearch

import org.scalatest.FunSpec

class KMPTest extends FunSpec {

  describe("Create Partial Match Table") {
    it("should return [-1] for a pattern with a single character") {
      assertResult(Array(-1)) {
        KMP.createTable("a")
      }
    }

    it("should return [-1, 0] for a pattern with two characters") {
      assertResult(Array(-1, 0)) {
        KMP.createTable("ag")
      }
    }

    it("should return [-1, 0, 0, 0, 0, 1, 2] for the pattern AGCTAGT") {
      assertResult(Array(-1, 0, 0, 0, 0, 1, 2)) {
        KMP.createTable("agctagt")
      }
    }

    it("should return [-1, 0, 0, 1, 0, 1, 2, 3, 2] for the pattern ABACABABC") {
      assertResult(Array(-1, 0, 0, 1, 0, 1, 2, 3, 2)) {
        KMP.createTable("agacagagc")
      }
    }
  }

  describe("testCount") (pending)

}
