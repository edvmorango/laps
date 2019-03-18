package app

import adapter.RaceInputStream
import error.CError
import service.{RaceService, RaceServiceImpl}

object Main extends App {

  val raceAdapter = new RaceInputStream()

  val inputStream = getClass.getResourceAsStream("/logs.txt")

  val raceService: RaceService = new RaceServiceImpl()

  raceAdapter.readRace(inputStream) match {
    case Right(r) =>
      val race = raceService.startRace(r, 4)
      val ranking = raceService.getRanking(race)

      val bestLaps = raceService.getPilotsBestLap(race)

      val bestLapOfRace = race.laps.minBy(_.lapTime)

      println(
        "Posição Chegada,Código Piloto,Nome Piloto,Qtde Voltas Completadas,Tempo Total de Prova")

      ranking.positions.foreach(p =>
        println(
          s"${p.position},${p.pilot.id},${p.pilot.name},${p.lapsBeforeEnd},${p.raceTime}"))

      println("\n\nMelhor volta dos pilotos:")
      println("Nome,Volta,Tempo")
      bestLaps.foreach(l =>
        println(s"${l.pilot.name},${l.lapNumber},${l.lapTime}"))

      println(s"\n\nMelhor volta da corrida:")
      println("Nome Piloto,Volta,Tempo")

      println(
        s"${bestLapOfRace.pilot.name},${bestLapOfRace.lapNumber},${bestLapOfRace.lapTime}")

      println("\n\nTempos de chegada após o vencedor:")
      println("Posição de Chegada,Nome Piloto,Tempo")
      ranking.positions.foreach(p =>
        println(s"${p.position},${p.pilot.name},${p.extraTimeToFinish}"))

    case Left(e) =>
      e match {
        case error.PilotParseError(input) => println(s"Pilot error: $input")
        case error.LapParseError(input)   => println(s"Lap error: $input")
        case error.EmptyInput             => println("Empty input")
        case e: CError                    => println(e)
      }
  }

}
