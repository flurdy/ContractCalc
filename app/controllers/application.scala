package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

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


object TimeController extends Controller {

  def hoursPerYear = Action {
    Ok(views.html.time.hoursPerYear())
  }

  def redirectHoursPerYear = Action {
    Redirect(routes.TimeController.hoursPerYear)
  }

  def calculateHoursPerYear = Action {
    Ok(views.html.time.hoursPerYear())
  }

}


object RateController extends Controller {

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
