package models

import org.scalatestplus.play._
import play.api.test.Helpers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class IncomeCalculatorSpec extends PlaySpec {

   trait Setup {
     lazy val incomeCalculator = IncomeCalculator(
       rate = 400,
       rateLength = RatePerDay,
       chargeUnitsPerYear = 200,
       cost = 0,
       utilisation = 100 )
   }

   "IncomeCalculator" should {

      "calculate yearly income by day" in new Setup {
         incomeCalculator.yearlyIncome.formatted mustBe "80,000"
      }

      "calculate yearly income by hour" in new Setup {
         incomeCalculator.copy(
            rate = 50,
            rateLength = RatePerHour,
            chargeUnitsPerYear = 1600).yearlyIncome.formatted mustBe "80,000"
      }

      "calculate costWeight 0" in new Setup {
         incomeCalculator.copy(cost = 0).costWeight mustBe 1
      }

      "calculate costWeight 10" in new Setup {
         incomeCalculator.copy(cost = 10).costWeight mustBe 0.9
      }

      "calculate utilWeight 0" in new Setup {
         incomeCalculator.copy(utilisation = 0).utilWeight mustBe 0
      }

      "calculate utilWeight 90" in new Setup {
         incomeCalculator.copy(utilisation = 90).utilWeight mustBe 0.9
      }

      "calculate utilWeight 100" in new Setup {
         incomeCalculator.copy(utilisation = 100).utilWeight mustBe 1
      }

      "calculate yearly income with utilisation" in new Setup {
         incomeCalculator.copy(utilisation = 90)
            .yearlyIncome.withUtilisation.formatted mustBe "72,000"
      }

      "calculate yearly income with utilisation and cost" in new Setup {
         incomeCalculator.copy(utilisation = 90).copy(cost = 10)
            .yearlyIncome.withUtilisation.withCost.formatted mustBe "64,800"
      }

   }


}
