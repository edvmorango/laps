package service

import domain._

trait RaceService {

  def startRace(laps: List[Lap], numberOfLaps: Long): Race

  def getBestLap(race: Race): Lap

  def getPilotsBestLap(race: Race): List[Lap]

  def getRanking(race: Race, complete: Boolean): Ranking
}

class RaceServiceImpl extends RaceService {

  def startRace(laps: List[Lap], numberOfLaps: Long): Race = {

    val (raceLaps, remainingLaps) = laps.span(_.lapNumber != numberOfLaps)

    val finalRaceLaps = raceLaps :+ remainingLaps.head
    val finalRemaining = remainingLaps.tail

    Race(numberOfLaps, finalRaceLaps, finalRemaining, raceLaps.reverse.head)

  }

  def getBestLap(race: Race): Lap = {

    race.laps.minBy(_.lapTime)

  }

  def getPilotsBestLap(race: Race): List[Lap] = {

    val groups = race.laps.groupBy(_.pilot.id)

    groups
      .map(g => g._2.minBy(_.lapTime))
      .toList

  }

  def getRanking(race: Race, complete: Boolean): Ranking = {

    type PartialRanking = (Pilot, Long, Double)

    def aggSpeed(lap: Lap, acc: (Double, Int)) =
      (acc._1 + lap.avgSpeed, acc._2 + 11)

    def aggRaceTime(lap: Lap, acc: Long) = acc + lap.lapTime

    val laps = if (complete) race.laps ++ race.remainingLaps else race.laps

    val groups = laps.groupBy(_.pilot).toList

    val partial: List[PartialRanking] = groups.map { g =>
      val (speed, laps) = g._2.foldRight((0.0, 0))(aggSpeed)

      val avgSpeed = speed / laps.toDouble
      val totalTime = g._2.foldRight(0L)(aggRaceTime)

      (g._1, totalTime, avgSpeed)

    }

    val positions: List[RankingPosition] =
      partial
        .foldLeft[List[RankingPosition]](Nil) { (acc, a) =>
          {
            if (acc.isEmpty)
              List(RankingPosition(a._1, a._2, a._3, 0))
            else
              RankingPosition(a._1, a._2, a._3, a._2 - acc.head.raceTime) :: acc
          }
        }
        .reverse

    Ranking(race, positions, complete)

  }

}
