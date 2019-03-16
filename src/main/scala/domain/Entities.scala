package domain

case class Pilot(id: Long, name: String)

case class Lap(startTime: Long,
               pilot: Pilot,
               lapNumber: Long,
               lapTime: Long,
               avgSpeed: Double)

case class Race(numberOfLaps: Long,
                laps: List[Lap],
                remainingLaps: List[Lap],
                lastLap: Lap)

case class RankingPosition(pilot: Pilot,
                           raceTime: Long,
                           avgSpeed: Double,
                           extraTime: Long)

case class Ranking(race: Race,
                   positions: List[RankingPosition],
                   complete: Boolean)
