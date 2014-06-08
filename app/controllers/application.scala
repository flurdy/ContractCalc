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
    Ok(views.html.hoursPerYear())
  }

}

object RateController extends Controller {

  def enterRate = Action {
    Ok(views.html.calculateRate())
  }

  def enterIncomeRate = Action {
    Ok(views.html.enterIncomeRate())
  }

  def rateDifference = Action {
    Ok(views.html.rateDifference())
  }

}