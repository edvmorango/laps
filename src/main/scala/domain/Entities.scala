package domain

case class Pilot(id: Long, name: String)

case class Lap(startTime: Long,
               pilot: Pilot,
               lapNumber: Long,
               lapTime: Long,
               avgSpeed: Double)

case class Race(laps: List[Lap], remainingLaps: List[Lap], lastLap: Lap)

case class AverageSpeed(race: Race, pilot: Pilot, speed: Double)
