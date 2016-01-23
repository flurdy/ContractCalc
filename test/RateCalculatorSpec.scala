package models

import org.scalatestplus.play._
import play.api.test.Helpers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class RateCalculatorSpec extends PlaySpec {

   trait Setup {
      lazy val rateCalculator = RateCalculator(
        salary       = 50000,
        hoursPerYear = 1600,
        cost         = 30,
        utilisation  = 90,
        tax          = -10,
        risk         = 0 )
   }

   "RateCalculator" should {

      "calculate taxWeight -10" in new Setup {
         rateCalculator.copy(tax = -10).taxWeight mustBe 0.9
      }

      "calculate taxWeight -0" in new Setup {
         rateCalculator.copy(tax = -0).taxWeight mustBe 1
      }

      "calculate taxWeight 0" in new Setup {
         rateCalculator.copy(tax = 0).taxWeight mustBe 1
      }

      "calculate taxWeight 10" in new Setup {
         rateCalculator.copy(tax = 10).taxWeight mustBe 1.1
      }

      "calculate costWeight 0" in new Setup {
         rateCalculator.copy(cost = 0).costWeight mustBe 1
      }

      "calculate costWeight 10" in new Setup {
         rateCalculator.copy(cost = 10).costWeight mustBe 1.1
      }

      "calculate utilWeight 0" in new Setup {
         rateCalculator.copy(utilisation = 0).utilWeight mustBe 0
      }

      "calculate utilWeight 90" in new Setup {
         rateCalculator.copy(utilisation = 90).utilWeight mustBe 0.9
      }

      "calculate utilWeight 100" in new Setup {
         rateCalculator.copy(utilisation = 100).utilWeight mustBe 1
      }

      "calculate yearly" in new Setup {
         rateCalculator.yearlyFormatted mustBe "52,650"
      }

      "calculate daily" in new Setup {
         rateCalculator.dailyFormatted(8) mustBe "264"
      }

      "calculate hourly" in new Setup {
         rateCalculator.hourlyFormatted mustBe "33"
      }

      "calculate 0 cost hourly" in new Setup {
         RateCalculator(
           salary       = 50000,
           hoursPerYear = 1600,
           cost         = 0,
           utilisation  = 100,
           tax          = 10,
           risk         = 10 )
         .yearlyFormatted mustBe "60,500"
      }

      "calculate 0 utilisation hourly" in new Setup {
         RateCalculator(
           salary       = 50000,
           hoursPerYear = 1600,
           cost         = 10,
           utilisation  = 0,
           tax          = 10,
           risk         = 10 )
         .yearlyFormatted mustBe "0"
      }

      "calculate 100 utilisation hourly" in new Setup {
         RateCalculator(
           salary       = 50000,
           hoursPerYear = 1600,
           cost         = 10,
           utilisation  = 100,
           tax          = 10,
           risk         = 10 )
         .yearlyFormatted mustBe "66,550"
      }

      "calculate 0 tax hourly" in new Setup {
         RateCalculator(
           salary       = 50000,
           hoursPerYear = 1600,
           cost         = 10,
           utilisation  = 100,
           tax          = 0,
           risk         = 10 )
         .yearlyFormatted mustBe "60,500"
      }

      "calculate 0 risk hourly" in new Setup {
         RateCalculator(
           salary       = 50000,
           hoursPerYear = 1600,
           cost         = 10,
           utilisation  = 100,
           tax          = 10,
           risk         = 0 )
         .yearlyFormatted mustBe "60,500"
      }

   }
}
