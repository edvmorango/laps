package service

import domain.{AverageSpeed, Lap, Race}

trait RaceService {

  def startRace(laps: List[Lap]): Race

  def getBestLap(race: Race): Lap

  def getPilotsBestLap(race: Race): List[Lap]

  def getPilotsAverageSpeed(laps: Race): List[AverageSpeed]

}
