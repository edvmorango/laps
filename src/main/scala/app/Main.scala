package app

import adapter.RaceInputStream

object Main extends App {

  val raceAdapter = new RaceInputStream()

  val inputStream = getClass.getResourceAsStream("/logs.txt")

  raceAdapter.readRace(inputStream) match {
    case Right(r) => println(r)
    case Left(e) =>
      e match {
        case error.PilotParseError(input) => println(s"Pilot error: $input")
        case error.LapParseError(input)   => println(s"Lap error: $input")
        case error.EmptyInput             => println("Empty input")
        case _                            => Unit
      }
  }

}
