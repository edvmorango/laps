package service

import domain._

trait RaceService {

  def startRace(laps: List[Lap], numberOfLaps: Long): Race

  def getBestLap(race: Race): Lap

  def getPilotsBestLap(race: Race): List[Lap]

  def getRanking(race: Race): Ranking

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

  def getRanking(race: Race): Ranking = {

    type PartialRanking = (Pilot, Long, Long, Double)

    val laps = race.laps ++ race.remainingLaps

    val groups = laps.groupBy(_.pilot).toList

    val partial: List[PartialRanking] = groups
      .map { g =>
        val (pilot, laps) = g

        val avgSpeed = laps.map(_.avgSpeed).sum / laps.length

        val totalTime =
          laps.foldRight(0L)((l, acc) => acc + l.lapTime)

        val endTime = totalTime + laps.head.startTime

        (pilot, endTime, totalTime, avgSpeed)

      }
      .sortBy(_._2)

    val firstEndTime = partial.head._2

    val positions: List[RankingPosition] = partial.map(a =>
      RankingPosition(a._1, a._2, a._3, a._4, a._2 - firstEndTime))

    Ranking(race, positions)

  }

}
