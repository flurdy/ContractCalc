package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._

class Application extends Controller {

  def index     = Action ( Ok(html.index()) )

  def resources = Action ( Ok(html.resources()) )

  def contact   = Action ( Ok(html.contact()) )

}


case class TimeForm(hoursPerDay: BigDecimal, holidays: Int, bankHolidays: Int,
                    sickDays: Int, trainingDays: Int, adminDays: Int, benchDays: Int)


class TimeController extends Controller {


  val timeForm = Form(
  	  mapping(
  	  	 "hoursPerDay"  -> default(bigDecimal(4,2), BigDecimal("8.0")),
  	  	 "holidays"     -> default(number(min = 0, max = 366), 25),
  	  	 "bankHolidays" -> default(number(min = 0, max = 366), 9),
         "sickDays"     -> default(number(min = 0, max = 366), 4),
  	  	 "trainingDays" -> default(number(min = 0, max = 366), 5),
  	  	 "adminDays"    -> default(number(min = 0, max = 366), 5),
  	  	 "benchDays"    -> default(number(min = 0, max = 366), 15)
  	  )(TimeForm.apply)(TimeForm.unapply) verifying ("hours per day not valid", fields =>
  	  		fields match {
  	  			case data => data.hoursPerDay < BigDecimal(24) && data.hoursPerDay > BigDecimal(0)
  	  		}
  	  )
  )

  def hoursPerYear = Action {  implicit request =>
    Ok(views.html.time.hoursPerYear(
      timeForm.fill( TimeForm(
            hoursPerDay  = BigDecimal("8").setScale(2),
            holidays     = 5,
            bankHolidays = 9,
            sickDays     = 4,
            trainingDays = 5,
            adminDays    = 5,
            benchDays    = 15 ))))
  }

  def redirectHoursPerYear = Action {
    Redirect(routes.TimeController.hoursPerYear)
  }

  def calculateHoursPerYear = Action { implicit request =>

	 timeForm.bindFromRequest.fold(
        errors => {
            Logger.warn("hoursPerYear failed: " + errors)
            BadRequest(views.html.time.hoursPerYear(errors))
        },
        times => {
          // todo
    			Ok(views.html.time.hoursPerYear(timeForm.fill(times)))
        }
    )
  }

}


case class CalculateRateForm(
  salary: Int, hoursPerYear: Int, cost: Int, utilisation: Int, tax: Int, risk: Int )


trait RateLength {
    val name: String
}

object RateLength {

  def apply(rateLength: String): RateLength = {
    rateLength.toLowerCase match {
      case "day" => RatePerDay
      case "hour" => RatePerHour
      case _ => throw new IllegalArgumentException("Unknown rate length")
    }
  }

  def unapply(rateLength: RateLength): Option[String] = Some(rateLength.name)

}

case object RatePerDay extends RateLength {
  val name = "day"
}

case object RatePerHour extends RateLength{
  val name = "hour"
}

case class CalculateIncomeForm( rate: Int, rateLength: RateLength, 
                                chargeUnitsPerYear: Int, cost: Int, utilisation: Int)

case class CalculateRateDifferenceForm(
        hoursPerYear: Int, hoursPerDay: BigDecimal,
        rateOne: Int, rateTwo: Int, rateLength: RateLength, benchTime: Int)


class RateController extends Controller {

  val calculateRateForm = Form(
      mapping(
          "salary"       -> default(number,50000),
          "hoursPerYear" -> number(min=0, max=3000),
          "cost"         -> number(min=0, max=100),
          "utilisation"  -> number(min=0, max=110),
          "tax"          -> number(min=50,max=100),
          "risk"         -> number(min=0, max=100)
      )(CalculateRateForm.apply)(CalculateRateForm.unapply)
    )

  def showEnterIncomeToCalculateRate = Action {
    Ok(views.html.rate.calculateRate(
       calculateRateForm.fill( CalculateRateForm(
        salary       = 50000,
        hoursPerYear = 1600,
        cost         = 30,
        utilisation  = 90,
        tax          = -10,
        risk         = 0
      ) ) ) )
  }

  def redirectEnterIncomeToCalculateRate = Action {
    Redirect(routes.RateController.showEnterIncomeToCalculateRate)
  }

  def calculateRateFromIncome = Action {
    // todo
    Redirect(routes.RateController.showEnterIncomeToCalculateRate)
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
      )(CalculateIncomeForm.apply)(CalculateIncomeForm.unapply)
    )

  def showEnterRateToCalculateIncome = Action {
    Ok(views.html.rate.calculateIncome(calculateIncomeForm.fill(
        CalculateIncomeForm(
          rate = 400,
          rateLength = RatePerDay,
          chargeUnitsPerYear = 210,
          cost = 20,
          utilisation = 90
      ))))
  }

  def redirectEnterRateToCalculateIncome = Action {
    Redirect(routes.RateController.showEnterRateToCalculateIncome)
  }

  def calculateIncomeFromRate = Action {
    // todo
    Redirect(routes.RateController.showEnterRateToCalculateIncome)
  }

  val calculateRateDifferenceForm = Form(
    mapping(
      "hoursPerYear" -> default(number(min=0,max=3000), 1600),
      "hoursPerDay"  ->  default(bigDecimal, BigDecimal("7.5").setScale(1)),
      "rateOne"      -> default(number(min=0), 400),
      "rateTwo"      -> default(number(min=0), 500),
      "rate-length"  -> mapping(
          "rate-length" -> default(nonEmptyText, "day")
        )(RateLength.apply)(RateLength.unapply),
      "benchTime"    -> default(number(min=0,max=365), 0)
      )(CalculateRateDifferenceForm.apply)(CalculateRateDifferenceForm.unapply)
    )

  def showRateDifference = Action {
    Ok(views.html.rate.rateDifference(calculateRateDifferenceForm.fill(
        CalculateRateDifferenceForm(
        hoursPerYear = 1600, hoursPerDay = BigDecimal("8").setScale(1),
        rateOne = 400, rateTwo = 500, rateLength = RatePerDay, benchTime = 0

    ))))
  }

  def redirectRateDifference = Action {
    Redirect(routes.RateController.showRateDifference)
  }

  def calculateRateDifference = Action {
    // todo
    Redirect(routes.RateController.showRateDifference)
  }

}
