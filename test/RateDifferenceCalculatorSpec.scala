package models

import org.scalatestplus.play._
import play.api.test.Helpers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class RateDifferenceCalculatorSpec extends PlaySpec {

   trait Setup {
      lazy val perDay = RateDifferenceCalculator(
        hoursPerYear = 1600,
        hoursPerDay = BigDecimal("8"),
        rateOne = 400,
        rateTwo = 500,
        rateLength = RatePerDay,
        benchTime = 0 )
      lazy val perHour = perDay.copy(
        rateOne = 40,
        rateTwo = 50,
        rateLength = RatePerHour )
      lazy val hourRateBenched = perDay.copy(
        benchTime = 10 )
      lazy val reversed = perDay.copy(
        rateTwo = 300 )
   }

   "RateDifferenceCalculator" should {

      "calculate yearly income per day" in new Setup {
         perDay.lower.formatted mustBe "80,000"
      }

      "calculate yearly income per hour" in new Setup {
         perHour.lower.formatted mustBe "64,000"
      }

      "calculate unbenched rate" in new Setup {
         perDay.higher.formatted mustBe "100,000"
      }

      "calculate benched rate" in new Setup {
         hourRateBenched.higher.formatted mustBe "95,000"
      }

      "calculate benched difference" in new Setup {
         hourRateBenched.difference.formatted mustBe "15,000"
      }

      "calculate rate 2 lower than 1" in new Setup {
         reversed.lower.formatted mustBe "60,000"
         reversed.higher.formatted mustBe "80,000"
         reversed.difference.formatted mustBe "20,000"
      }
   }

}
