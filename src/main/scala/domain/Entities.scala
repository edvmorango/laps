package domain

case class Pilot(id: Int, name: String) {
  override def equals(obj: Any): Boolean = obj match {
    case o: Pilot => o.id == this.id
    case _        => false
  }
}

case class Lap(startTime: Long,
               pilot: Pilot,
               lapNumber: Int,
               lapTime: Long,
               avgSpeed: Double)

case class Race(numberOfLaps: Int,
                laps: List[Lap],
                remainingLaps: List[Lap],
                lastLap: Lap,
)

case class RankingPosition(position: Int,
                           pilot: Pilot,
                           endTime: Long,
                           raceTime: Long,
                           avgSpeed: Double,
                           lapsBeforeEnd: Int,
                           extraTimeToFinish: Long)

case class Ranking(race: Race, positions: List[RankingPosition])
