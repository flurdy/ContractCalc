package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models._
import play.api.Play.current

trait ConfigurationHelper {

  def getConfigurationString(key: String): Option[String] = Play.configuration.getString(key)

}


object AnalyticsHelper extends Controller with ConfigurationHelper {

    implicit val analyticsId: Option[Analytics] = getConfigurationString("analytics.id").map( Analytics(_))

    def analyticsTemplate = html.analytics()(analyticsId)

}

object AdsenseHelper extends Controller with ConfigurationHelper {

    implicit val adsenseDetails: Option[Adsense] = {
      for {
        client <- getConfigurationString("adsense.client")
        slot   <- getConfigurationString("adsense.skyscraper.slot")
      } yield Adsense(client,slot)
    }

    def adsenseTemplate = html.adsense.skyscraper()(adsenseDetails)

}


class Application extends Controller {

  def index     = Action ( Ok(html.index()) )

  def resources = Action ( Ok(html.resources()) )

  def contact   = Action ( Ok(html.contact()) )

}


class TimeController extends Controller {

  val perYearCalculation = Form(
  	  mapping(
  	  	 "hoursPerDay"  -> default(bigDecimal, BigDecimal("8.0")),
  	  	 "holidays"     -> default(number(min = 0, max = 366), 25),
  	  	 "bankHolidays" -> default(number(min = 0, max = 366), 9),
         "sickDays"     -> default(number(min = 0, max = 366), 4),
  	  	 "trainingDays" -> default(number(min = 0, max = 366), 5),
  	  	 "adminDays"    -> default(number(min = 0, max = 366), 5),
  	  	 "benchDays"    -> default(number(min = 0, max = 366), 15)
  	  )(PerYearCalculation.apply)(PerYearCalculation.unapply) verifying ("hours per day not valid", fields =>
  	  		fields match {
  	  			case data => data.hoursPerDay < BigDecimal(24) && data.hoursPerDay > BigDecimal(0)
  	  		}
  	  )
  )

  def hoursPerYear = Action {  implicit request =>
    Ok(views.html.time.hoursPerYear(
      perYearCalculation.fill( PerYearCalculation(
            hoursPerDay  = BigDecimal("8").setScale(0),
            holidays     = 25,
            bankHolidays = 9,
            sickDays     = 4,
            trainingDays = 5,
            adminDays    = 5,
            benchDays    = 15 )), None))
  }

  def redirectHoursPerYear = Action {
    Redirect(routes.TimeController.hoursPerYear)
  }

  def calculateHoursPerYear = Action { implicit request =>
	  perYearCalculation.bindFromRequest.fold(
      errors => BadRequest(views.html.time.hoursPerYear(errors,None)),
      calculation => Ok(views.html.time.hoursPerYear(
                          perYearCalculation.fill(calculation),Some(calculation)))
    )
  }

}


class RateController extends Controller {

  val calculateRateForm = Form(
      mapping(
          "salary"       -> default(number,50000),
          "hoursPerYear" -> number(min=0, max=3000),
          "cost"         -> number(min=0, max=100),
          "utilisation"  -> number(min=0, max=110),
          "tax"          -> number(min= -50, max=100),
          "risk"         -> number(min=0, max=100)
      )(RateCalculator.apply)(RateCalculator.unapply)
    )

  def showEnterIncomeToCalculateRate = Action {
    Ok( views.html.rate.calculateRate(
       calculateRateForm.fill( RateCalculator(
        salary       = 50000,
        hoursPerYear = 1600,
        cost         = 30,
        utilisation  = 90,
        tax          = -10,
        risk         = 0 ) ), None ) )
  }

  def redirectEnterIncomeToCalculateRate = Action {
    Redirect(routes.RateController.showEnterIncomeToCalculateRate)
  }

  def calculateRateFromIncome = Action { implicit request =>
    calculateRateForm.bindFromRequest.fold(
      errors => BadRequest(views.html.rate.calculateRate(errors,None)),
      calculation => Ok(views.html.rate.calculateRate(
                          calculateRateForm.fill(calculation),Some(calculation)))
    )
  }


  val calculateIncomeForm = Form(
      mapping(
          "rate"         -> default(number, 400),
          "rate-length"   -> mapping(
            "rate-length" -> default(nonEmptyText,"day")
          )(RateLength.apply)(RateLength.unapply),
          "charge-units-per-year" -> number(min=1,max=3000),
          "cost"         -> number(min=0,max=100),
          "utilisation"  -> number(min=0,max=110)
      )(IncomeCalculator.apply)(IncomeCalculator.unapply)
    )

  def showEnterRateToCalculateIncome = Action {
    Ok(views.html.rate.calculateIncome(calculateIncomeForm.fill(
        IncomeCalculator(
          rate = 400,
          rateLength = RatePerDay,
          chargeUnitsPerYear = 210,
          cost = 20,
          utilisation = 90
      ) ), None ) )
  }

  def redirectEnterRateToCalculateIncome = Action {
    Redirect(routes.RateController.showEnterRateToCalculateIncome)
  }

  def calculateIncomeFromRate = Action { implicit request =>
    calculateIncomeForm.bindFromRequest.fold(
      errors => BadRequest(views.html.rate.calculateIncome(errors,None)),
      calculation => Ok(views.html.rate.calculateIncome(
                          calculateIncomeForm.fill(calculation),Some(calculation)))
    )
  }


  val calculateRateDifferenceForm = Form(
    mapping(
      "hoursPerYear" -> default(number(min=0,max=3000), 1600),
      "hoursPerDay"  -> default(bigDecimal, BigDecimal("7.5").setScale(1)),
      "rateOne"      -> number(min=0),
      "rateTwo"      -> number(min=0),
      "rate-length"  -> mapping(
          "rate-length" -> default(nonEmptyText, "day")
        )(RateLength.apply)(RateLength.unapply),
      "benchTime"    -> default(number(min=0,max=365), 0)
      )(RateDifferenceCalculator.apply)(RateDifferenceCalculator.unapply)
    )

  def showRateDifference = Action {
    Ok(views.html.rate.rateDifference(calculateRateDifferenceForm.fill(
        RateDifferenceCalculator(
          hoursPerYear = 1600,
          hoursPerDay = BigDecimal("8").setScale(1),
          rateOne = 400,
          rateTwo = 500,
          rateLength = RatePerDay,
          benchTime = 0
        ) ), None ) )
  }

  def redirectRateDifference = Action {
    Redirect(routes.RateController.showRateDifference)
  }

  def calculateRateDifference = Action { implicit request =>
    calculateRateDifferenceForm.bindFromRequest.fold(
      errors => BadRequest(views.html.rate.rateDifference(errors,None)),
      calculation => Ok(views.html.rate.rateDifference(
                          calculateRateDifferenceForm.fill(calculation),Some(calculation)))
    )
  }

}
