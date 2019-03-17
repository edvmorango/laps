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

        val bestLaps = lapsM.map(l => l.minBy(_.lapTime).lapTime).sorted

        raceService.getPilotsBestLap(race).map(_.lapTime).sorted mustBe bestLaps
      }

      "Pilots average speed" in {

        val avgSpeedList = lapsM
          .map(l => l.map(_.avgSpeed).sum / 4)
          .sortWith(_ > _)

        raceService
          .getRanking(race)
          .positions
          .map(_.avgSpeed) mustBe avgSpeedList

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
