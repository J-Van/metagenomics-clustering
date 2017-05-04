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

    it("should return [-1, 0, 0, 1, 0, 1, 2, 3, 2] for the pattern AGACAGAGC") {
      assertResult(Array(-1, 0, 0, 1, 0, 1, 2, 3, 2)) {
        KMP.createTable("agacagagc")
      }
    }

    it("should return [-1, 0, 1, 2, 0, 1, 2, 3, 3, 3] for the pattern AAABAAAAAB") {
      assertResult(Array(-1, 0, 1, 2, 0, 1, 2, 3, 3, 3)) {
        KMP.createTable("aaabaaaaab")
      }
    }
  }

  describe("Count the patterns") {
    it("should return 6 for the pattern A in AAAAAA") {
      assertResult(6) {
        KMP.count("a", "aaaaaa")
      }
    }

    it("should return 3 for the pattern AAAA in AAAAAA") {
      assertResult(3) {
        KMP.count("aaaa", "aaaaaa")
      }
    }

    it("should return 2 for the pattern ABA in ABCABABCABAB") {
      assertResult(2) {
        KMP.count("aba", "abcababcabab")
      }
    }

    it("should return 4 for the pattern AGCA in AGATCAGCAGCATAGCATGAGCA") {
      assertResult(4) {
        KMP.count("agca", "agatcagcagcatagcatgagca")
      }
    }
  }

}
