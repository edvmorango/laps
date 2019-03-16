package adapter

import java.io.InputStream

import domain.{Lap, Pilot}
import error._

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Try

class RaceInputStream() {

  private type ELaps = Either[CError, List[Lap]]

  def readRace(stream: InputStream): Either[CError, List[Lap]] = {

    val lines = Source.fromInputStream(stream).getLines().toList

    lines match {
      case Nil => Left(EmptyInput)
      case _ :: lines =>
        val laps = readLaps(lines, Right(Nil))
        laps match {
          case Right(laps) => Right(laps.reverse)
          case Left(e)     => Left(e)
        }
    }

  }

  @tailrec
  private def readLaps(lines: List[String], acc: ELaps): ELaps = {
    (acc.isLeft, lines) match {
      case (true, _) => acc
      case (_, Nil)  => acc
      case (_, h :: t) =>
        val lap = parseLap(h)
        if (lap.isLeft)
          readLaps(Nil, Left(lap.left.get))
        else {
          val nAcc = lap.right.get :: acc.right.get
          readLaps(t, Right(nAcc))
        }
    }

  }

  private def parseLap(line: String): Either[ParseError, Lap] = {

    def parsePilot(stretch: String): Either[PilotParseError, Pilot] = {
      stretch.trim
        .split(" – ")
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

    def parseLapNumber(lapNumber: String): Either[ParseError, Long] =
      Try(lapNumber.trim.toLong).toEither
        .fold(_ => Left(LapParseError(lapNumber)), v => Right(v))

    def parseAvgSpeed(avgSpeed: String): Either[ParseError, Double] =
      Try(avgSpeed.trim.replace(',', '.').toDouble).toEither
        .fold(_ => Left(LapParseError(avgSpeed)), v => Right(v))

    line.trim
      .split("  ")
      .filterNot(_.isBlank)
      .toList match {
      case startTime :: pilotStretch :: lapNumber :: lapTime :: avgSpeed :: Nil =>
        val lap = for {
          pStartTime <- TimeParser.validTimeToMillis(startTime)
          pilot <- parsePilot(pilotStretch)
          plapNumber <- parseLapNumber(lapNumber)
          pLapTime <- TimeParser.validTimeToMillis(lapTime)
          pAvgSpeed <- parseAvgSpeed(avgSpeed)
        } yield Lap(pStartTime, pilot, plapNumber, pLapTime, pAvgSpeed)

        lap.fold(
          {
            case LapParseError(e) =>
              Left(LapParseError(s"Lap Parse error in: [$e] at [$line]"))
            case PilotParseError(e) =>
              Left(LapParseError(s"Pilot parse error in: [$e] at lap [$line]"))
            case TimeParserError(e) =>
              Left(LapParseError(s"Lap Parse error in: [$e] at [$line]"))
          },
          Right(_)
        )

      case _ => Left(LapParseError(line))

    }

  }

}

object TimeParser {

  private val MILLIS: Long = 1
  private val SECONDS: Long = 1000
  private val MINUTES: Long = 60 * 1000
  private val HOURS: Long = 60 * 60 * 1000

  private val FACTOR: List[Long] = List(MILLIS, SECONDS, MINUTES, HOURS)

  def validTimeToMillis(time: String): Either[TimeParserError, Long] =
    Try {
      val reversed = time.trim.split(':').reverse.toList

      val secAndMillis = reversed.head.split('.').reverse

      val times = (if (secAndMillis.length == 1) "0" +: secAndMillis
                   else secAndMillis) ++ reversed.tail

      times
        .map(_.toLong)
        .zip(FACTOR)
        .foldRight(0L)((tf, acc) => (tf._1 * tf._2) + acc)

    }.toEither
      .fold(_ => Left(TimeParserError(time)), v => Right(v))

  def millisToTime(totalMillis: Long): String = {

    val millis = totalMillis % SECONDS

    val totalSeconds = totalMillis / SECONDS

    val totalMinutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    "%02d:%02d:%02d.%d".format(hours, minutes, seconds, millis)
  }

}
