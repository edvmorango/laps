package adapter

import error.PilotParseError
import org.scalatest.{MustMatchers, WordSpec}

class RaceAdapterSpec extends WordSpec with MustMatchers {

  val raceAdapter: RaceInputStream = new RaceInputStream

  "Race Adapter" should {

    "Read a valid race file" in {

      val inputStream = getClass.getResourceAsStream("/logs.txt")

      raceAdapter.readRace(inputStream).isRight mustBe true

    }

    "Fail to read a log with invalid hours " in {

      val inputStream = getClass.getResourceAsStream("/invalidHourLog.txt")

      raceAdapter.readRace(inputStream).isLeft mustBe true

    }

    "Fail to read a log with a invalid pilot " in {

      val inputStream = getClass.getResourceAsStream("/invalidPilotLog.txt")

      raceAdapter
        .readRace(inputStream)
        .left
        .get
        .isInstanceOf[PilotParseError] mustBe true

    }

    "Fail to read a log with a invalid lap number " in {

      val inputStream = getClass.getResourceAsStream("/invalidLapNumberLog.txt")

      raceAdapter.readRace(inputStream).isLeft mustBe true

    }

    "Fail to read a log with a invalid average speed" in {

      val inputStream =
        getClass.getResourceAsStream("/invalidAverageSpeedLog.txt")

      raceAdapter.readRace(inputStream).isLeft mustBe true

    }
  }

}
