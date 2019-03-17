package app

import adapter.RaceInputStream
import service.{RaceService, RaceServiceImpl}

object Main extends App {

  val raceAdapter = new RaceInputStream()

  val inputStream = getClass.getResourceAsStream("/logs.txt")

  val raceService: RaceService = new RaceServiceImpl()

  raceAdapter.readRace(inputStream) match {
    case Right(r) =>
      val race = raceService.startRace(r, 4)
      println(race)
      println(raceService.getBestLap(race))
      println(raceService.getPilotsBestLap(race))
      println(raceService.getRanking(race))

    case Left(e) =>
      e match {
        case error.PilotParseError(input) => println(s"Pilot error: $input")
        case error.LapParseError(input)   => println(s"Lap error: $input")
        case error.EmptyInput             => println("Empty input")
        case _                            => Unit
      }
  }

}
