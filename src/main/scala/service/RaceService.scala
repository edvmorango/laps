package service

import domain._

trait RaceService {

  def startRace(laps: List[Lap], numberOfLaps: Int): Race

  def getBestLap(race: Race): Lap

  def getPilotsBestLap(race: Race): List[Lap]

  def getRanking(race: Race): Ranking

}

class RaceServiceImpl extends RaceService {

  def startRace(laps: List[Lap], numberOfLaps: Int): Race = {

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

  def getRanking(race: Race): Ranking = {

    type PartialRanking = (Pilot, Long, Long, Double, List[(Int, Long)])

    val laps = race.laps ++ race.remainingLaps

    val pilots = laps.map(_.pilot).distinct

    val groups = laps.groupBy(_.pilot.id).toList

    val partial: List[PartialRanking] = groups
      .map { g =>
        val (pilotId, laps) = g

        val avgSpeed = laps.map(_.avgSpeed).sum / laps.length

        val totalTime =
          laps.foldRight(0L)((l, acc) => acc + l.lapTime)

        val endTime = totalTime + laps.head.startTime

        val lapsFinishTime =
          laps.map(l => (l.lapNumber, l.startTime + l.lapTime))

        (pilots.find(_.id == pilotId).get,
         endTime,
         totalTime,
         avgSpeed,
         lapsFinishTime)

      }
      .sortBy(_._2)

    val firstEndTime = partial.head._2

    val positions: List[RankingPosition] = partial.zipWithIndex.map { ai =>
      val (r, p) = ai

      val laps = r._5.filter(_._2 <= firstEndTime).map(_._1)

      val lapBeforeEnd = if (laps.isEmpty) 0 else laps.max

      RankingPosition(p + 1,
                      r._1,
                      r._2,
                      r._3,
                      r._4,
                      lapBeforeEnd,
                      r._2 - firstEndTime)

    }
    Ranking(race, positions)

  }

}
