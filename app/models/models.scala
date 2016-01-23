package models


trait RateLength {
    val name: String
}

case object RatePerDay extends RateLength {
  val name = "day"
}

case object RatePerHour extends RateLength{
  val name = "hour"
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


case class PerYear(maximumWeekDays: Int, hoursPerDay: BigDecimal, days: Int){
  val weeks: Int        = days / 5
  val hours: BigDecimal = (days * hoursPerDay).setScale(0 )
  val percentage: Int   = Math.floor( (days * 100)/ maximumWeekDays ).toInt
}


case class PerYearCalculation(hoursPerDay: BigDecimal, holidays: Int, bankHolidays: Int,
                    sickDays: Int, trainingDays: Int, adminDays: Int, benchDays: Int){
  private val maximumWeekDays = 365 / 7 * 5
  val unrealistic = PerYear(maximumWeekDays, hoursPerDay, maximumWeekDays - bankHolidays)
  val optimistic  = PerYear(maximumWeekDays, hoursPerDay, unrealistic.days - holidays - trainingDays - adminDays)
  val realistic   = PerYear(maximumWeekDays, hoursPerDay, optimistic.days - sickDays - benchDays)
}


case class RateCalculator( salary: Int, hoursPerYear: Int,
                           cost: Int, utilisation: Int, tax: Int, risk: Int ){

   require(cost >= 0, "Cost can not be negative")
   require(risk >= 0, "Risk can not be negative")
   require(utilisation <= 100, "Utilisation can not be more than 100")

  val taxWeight  = if(tax  == 0) 1 else 1 + (tax.toDouble / 100)
  val costWeight = if(cost == 0) 1 else 1 + (cost.toDouble / 100)
  val riskWeight = if(risk == 0) 1 else 1 + (risk.toDouble / 100)
  val utilWeight = if(utilisation == 0) 0 else utilisation.toDouble / 100

  val yearly: BigDecimal = (BigDecimal(1) * salary * costWeight * taxWeight * utilWeight * riskWeight)
      .setScale(0,BigDecimal.RoundingMode.HALF_UP)

  val hourly: BigDecimal = (yearly / hoursPerYear).setScale(0,BigDecimal.RoundingMode.HALF_UP)

  lazy val yearlyFormatted = IntegerFormatter.formatter.format( yearly )
  lazy val hourlyFormatted = IntegerFormatter.formatter.format( hourly )

  def daily(hours: BigDecimal) = (hourly * hours).setScale(0,BigDecimal.RoundingMode.HALF_UP)

  def dailyFormatted(hours: BigDecimal) = IntegerFormatter.formatter.format( daily(hours) )
}



case class IncomeCalculator( rate: Int, rateLength: RateLength,
                             chargeUnitsPerYear: Int, cost: Int, utilisation: Int){

   require(cost >= 0, "Cost can not be negative")
   require(chargeUnitsPerYear >= 0, "ChargeUnitsPerYear can not be negative")
   require(utilisation <= 100, "Utilisation can not be more than 100")

   val costWeight = if(cost == 0) 1 else 1 - (cost.toDouble / 100)
   val utilWeight = if(utilisation == 0) 0 else utilisation.toDouble / 100

   trait YearlyIncome {
      def income: BigDecimal
      lazy val formatted = IntegerFormatter.formatter.format( income )
      def rounded(value: BigDecimal) = value.setScale(0,BigDecimal.RoundingMode.HALF_UP)
   }

   val yearlyIncome = new YearlyIncome {
      private val maxIncome = BigDecimal(1) * rate * chargeUnitsPerYear
      override val income = rounded(maxIncome)
      val withUtilisation = new YearlyIncome {
         private val utilisationIncome = maxIncome * utilWeight
         override val income = rounded(utilisationIncome)
         val withCost = new YearlyIncome {
            override val income = rounded(utilisationIncome * costWeight)
         }
      }
   }
}


case class IncomeValue(rawIncome: BigDecimal){
   val income: BigDecimal = rounded(rawIncome)
   lazy val formatted = IntegerFormatter.formatter.format( income )
   def rounded(value: BigDecimal) = value.setScale(0,BigDecimal.RoundingMode.HALF_UP)
}


case class AnnualRate(rate: Int, rateLength: RateLength, daysPerYear: BigDecimal, hoursPerDay: BigDecimal){
   val ratePerDay = (rateLength != RatePerDay).fold[BigDecimal](rate)(_ * hoursPerDay)
   val annual     = IncomeValue(ratePerDay * daysPerYear)
   val monthly    = IncomeValue(annual.income / 12)
   val formatted  = annual.formatted
}

case class RateDifferenceCalculator(
        hoursPerYear: Int, hoursPerDay: BigDecimal,
        rateOne: Int, rateTwo: Int, rateLength: RateLength, benchTime: Int){

   require(benchTime >= 0, "BenchTime can not be negative")

   private val (lowerRate,higherRate) = if (rateOne <= rateTwo) (rateOne,rateTwo) else (rateTwo,rateOne)

   private val daysPerYear = hoursPerYear / hoursPerDay
   val lower      = AnnualRate(lowerRate, rateLength, daysPerYear, hoursPerDay)
   val higher     = AnnualRate(higherRate, rateLength, daysPerYear - benchTime, hoursPerDay)
   val difference = IncomeValue( higher.annual.income - lower.annual.income )
   private val higherUnbenched = AnnualRate(higherRate, rateLength, daysPerYear, hoursPerDay)
   val monthlyDifference = IncomeValue( higherUnbenched.monthly.income - lower.monthly.income )
}
