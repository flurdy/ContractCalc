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
  	  				hoursPerDay < BigDecimal(24) && hoursPerDay > BigDecimal(0)   	  			}
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

  def enterRate = Action {
    Ok(views.html.rate.calculateRate())
  }

  def redirectEnterRate = Action {
    Redirect(routes.RateController.enterRate)
  }

  def calculateRate = Action {
    Ok(views.html.rate.calculateRate())
  }

  def enterIncomeRate = Action {
    Ok(views.html.rate.enterIncomeRate())
  }

  def redirectEnterIncomeRate = Action {
    Redirect(routes.RateController.enterIncomeRate)
  }

  def calculateIncome = Action {
    Ok(views.html.rate.enterIncomeRate())
  }

  def rateDifference = Action {
    Ok(views.html.rate.rateDifference())
  }

  def redirectRateDifference = Action {
    Redirect(routes.RateController.rateDifference)
  }

  def calculateRateDifference = Action {
    Ok(views.html.rate.rateDifference())
  }

}
