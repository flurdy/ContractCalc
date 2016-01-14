package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def resources = Action {
    Ok(views.html.resources())
  }

  def contact = Action {
    Ok(views.html.contact())
  }

}


class TimeController extends Controller {

  val timeForm = Form(
  	  tuple(
  	  	 "hoursPerDay"  -> bigDecimal,
  	  	 "holidays"     -> number(min = 0, max = 366),
  	  	 "bankHolidays" -> number(min = 0, max = 366),
  	  	 "sickDays"     -> number(min = 0, max = 366),
  	  	 "trainingDays" -> number(min = 0, max = 366),
  	  	 "adminDays"    -> number(min = 0, max = 366),
  	  	 "benchDays"    -> number(min = 0, max = 366)
  	  ) verifying ("hours per day not valid", fields =>
  	  		fields match {
  	  			case (hoursPerDay, _, _, _, _, _, _) => {
  	  				hoursPerDay < BigDecimal(24) && hoursPerDay > BigDecimal(0)
          	}
  	  		}
  	  )
  )

  def hoursPerYear = Action {
    Ok(views.html.time.hoursPerYear())
  }

  def redirectHoursPerYear = Action {
    Redirect(routes.TimeController.hoursPerYear)
  }

  def calculateHoursPerYear = Action { implicit request =>

	 timeForm.bindFromRequest.fold(
        errors => {
            Logger.warn("Contact failed: " + errors)
            BadRequest(views.html.time.hoursPerYear(Some(errors)))
        },
        times => {
        		// todo
    			Ok(views.html.time.hoursPerYear(Some(timeForm)))
        }
    )
  }

}


class RateController extends Controller {

  def showEnterIncomeToCalculateRate = Action {
    Ok(views.html.rate.calculateRate())
  }

  def redirectEnterIncomeToCalculateRate = Action {
    Redirect(routes.RateController.showEnterIncomeToCalculateRate)
  }

  def calculateRateFromIncome = Action {
    Redirect(routes.RateController.showEnterIncomeToCalculateRate)
  }

  def showEnterRateToCalculateIncome = Action {
    Ok(views.html.rate.calculateIncome())
  }

  def redirectEnterRateToCalculateIncome = Action {
    Redirect(routes.RateController.showEnterRateToCalculateIncome)
  }

  def calculateIncomeFromRate = Action {
    Redirect(routes.RateController.showEnterRateToCalculateIncome)
  }

  def showRateDifference = Action {
    Ok(views.html.rate.rateDifference())
  }

  def redirectRateDifference = Action {
    Redirect(routes.RateController.showRateDifference)
  }

  def calculateRateDifference = Action {
    Redirect(routes.RateController.showRateDifference)
  }

}
