import co.edu.eia.metagenomics.kmers.KmerCount
import org.scalameter.api._

import scala.util.Random

object KmerCountMicrobenchmark extends Bench.LocalTime {
  val sizes: Gen[Int] = Gen.range("size")(50000, 250000, 50000)
  val sequences: Gen[String] = for {
    size <- sizes
  } yield (1 to size).map(x => "ACGT"(Random.nextInt.abs % 4)).mkString

  performance of "KmerCount" in {
    measure method "apply" in {
      using(sequences) in {
        sequence => KmerCount(4, sequence)
      }
    }
  }
}
