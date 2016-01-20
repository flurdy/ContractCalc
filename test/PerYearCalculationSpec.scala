package models

import org.scalatestplus.play._
import play.api.test.Helpers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import controllers.PerYearCalculation

class PerYearCalculationSpec extends PlaySpec {

   trait Setup {
      val perYearCalculation =
         PerYearCalculation(
            hoursPerDay  = BigDecimal("10").setScale(1),
            holidays     = 25,
            bankHolidays = 9,
            sickDays     = 4,
            trainingDays = 5,
            adminDays    = 5,
            benchDays    = 15 )
   }

   "PerYearCalculation" should {

      "calculate max working days per year" in new Setup {
         perYearCalculation.unrealistic.maximumWeekDays mustBe 260
      }

      "calculate unrealistic days per year" in new Setup {
         perYearCalculation.unrealistic.days mustBe 251
      }

      "calculate optimistic days per year" in new Setup {
         perYearCalculation.optimistic.days mustBe 216
      }

      "calculate realistic days per year" in new Setup {
         perYearCalculation.realistic.days mustBe 197
      }

      "calculate realistic hours per year" in new Setup {
         perYearCalculation.realistic.hours mustBe 1970
      }

      "calculate realistic weeks per year" in new Setup {
         perYearCalculation.realistic.weeks mustBe 39
      }

      "calculate percentage of per year" in new Setup {
         perYearCalculation.realistic.percentage mustBe 75
      }

   }


}