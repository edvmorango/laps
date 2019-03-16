package adapter

import java.io.InputStream

import domain.{Lap, Pilot}
import error.{CError, EmptyInput, PilotParseError}

import scala.io.Source

class RaceInputStream {

  def readRace(stream: InputStream): Either[CError, Unit] = {

    val lines = Source.fromInputStream(stream).getLines().toList

    lines match {
      case Nil        => EmptyInput()
      case _ :: lines => ???
    }

  }

  private def parsePilot(stretch: String): Either[PilotParseError, Pilot] = {
    stretch.trim
      .split(" - ")
      .toList match {
      case id :: name :: Nil =>
        if (id.forall(_.isDigit))
          Right(Pilot(id.toLong, name))
        else
          Left(PilotParseError(stretch))
      case _ =>
        Left(PilotParseError(stretch))
    }
  }

  private def parseLap(line: String): Either[CError, Lap] = ???

}
