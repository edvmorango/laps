package fixtures

import domain.{Lap, Pilot}

object LapsFixtures {

  val pilotA = Pilot(1, "A")
  val pilotB = Pilot(2, "B")
  val pilotC = Pilot(3, "C")

  val lapA1 = Lap(5000000, pilotA, 1, 52852, 56.5)
  val lapA2 = Lap(5052852, pilotA, 2, 72852, 48.5)
  val lapA3 = Lap(5125704, pilotA, 3, 52752, 56.7)
  val lapA4 = Lap(5178456, pilotA, 4, 52800, 56.4)

  val lapB1 = Lap(5005000, pilotB, 1, 52852, 56.5)
  val lapB2 = Lap(5057852, pilotB, 2, 39990, 91.5)
  val lapB3 = Lap(5097842, pilotB, 3, 70323, 30.7)
  val lapB4 = Lap(5168165, pilotB, 4, 52800, 56.4)

  val lapC1 = Lap(5000000, pilotC, 1, 40000, 90.5)
  val lapC2 = Lap(5040000, pilotC, 2, 50000, 55.5)
  val lapC3 = Lap(5090000, pilotC, 3, 50000, 55.5)
  val lapC4 = Lap(5140000, pilotC, 4, 52800, 56.4)

  val lapsA = List(lapA1, lapA2, lapA3, lapA4)
  val lapsB = List(lapB1, lapB2, lapB3, lapB4)
  val lapsC = List(lapC1, lapC2, lapC3, lapC4)

  val laps: List[Lap] = (lapsA ++ lapsB ++ lapsC).sortBy(_.startTime)

  val lapsM = List(lapsA, lapsB, lapsC)
}
