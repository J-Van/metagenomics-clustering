import co.edu.eia.metagenomics.kmers.KmerCount
import org.scalameter.api.{Bench, Gen}

import scala.util.Random

object KmerCountMicrobenchmark extends Bench.LocalTime {
  val alphabet = "ACGT"
  val sequences: Gen[String] = for {
    size <- Gen.range("size")(50000, 250000, 50000)
  } yield (1 to size).map(_ => alphabet(Random.nextInt.abs % alphabet.length)).mkString

  performance of "KmerCount" in {
    measure method "apply" in {
      using(sequences) in {
        sequence => KmerCount(4, sequence)
      }
    }
  }
}
