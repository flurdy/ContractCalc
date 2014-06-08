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

  def calculateHoursPerYear = Action {
    Ok(views.html.time.hoursPerYear())
  }

}


object RateController extends Controller {

  def enterRate = Action {
    Ok(views.html.rate.calculateRate())
  }

  def calculateRate = Action {
    Ok(views.html.rate.calculateRate())
  }

  def enterIncomeRate = Action {
    Ok(views.html.rate.enterIncomeRate())
  }

  def calculateIncome = Action {
    Ok(views.html.rate.enterIncomeRate())
  }

  def rateDifference = Action {
    Ok(views.html.rate.rateDifference())
  }

  def calculateRateDifference = Action {
    Ok(views.html.rate.rateDifference())
  }

}
