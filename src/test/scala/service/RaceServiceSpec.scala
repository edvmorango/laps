package service

import org.scalatest.{MustMatchers, WordSpec}
import fixtures.LapsFixtures._

class RaceServiceSpec extends WordSpec with MustMatchers {

  val raceService: RaceService = new RaceServiceImpl()

  "Race Service Spec" should {

    "Start a 4 laps race" should {

      val race = raceService.startRace(laps, 4)

      "PilotC wins" in {

        raceService
          .getRanking(race)
          .positions
          .head
          .pilot mustBe pilotC

      }

      "PilotB have the best lap of the race" in {

        raceService
          .getPilotsBestLap(race)
          .head
          .pilot mustBe pilotB

      }

      "Pilots best lap " in {

        val bestA = lapsA.minBy(_.lapTime)
        val bestB = lapsB.minBy(_.lapTime)
        val bestC = lapsC.minBy(_.lapTime)

        raceService.getPilotsBestLap(race).map(_.lapTime).sorted mustBe List(
          bestA,
          bestB,
          bestC).map(_.lapTime).sorted

      }

      "Pilots average speed" in {

        val pilotASpeed = lapsA.map(_.avgSpeed).sum / 4
        val pilotBSpeed = lapsB.map(_.avgSpeed).sum / 4
        val pilotCSpeed = lapsC.map(_.avgSpeed).sum / 4

        raceService
          .getRanking(race)
          .positions
          .map(_.avgSpeed) mustBe List(pilotASpeed, pilotBSpeed, pilotCSpeed)
          .sortWith(_ > _)

      }

      "Pilots difference between first in time" in {

        val lasts =
          lapsM
            .map(_.find(l => l.lapNumber == 4).get)
            .map(l => l.startTime + l.lapTime)
            .sorted

        val head = lasts.head

        val diffs = lasts.map(_ - head)

        raceService.getRanking(race).positions.map(_.extraTime) mustBe diffs

      }

    }

  }

}
