package models;

import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class RateDifferenceCalculation {


    @Required(message = "Incorrect hours per year")
    @Min(100)
    @Max(5000)
    public BigDecimal hoursPerYear;

    @Required(message = "Incorrect hours per day")
    @Min(1)
    @Max(24)
    public BigDecimal hoursPerDay;

    @Required(message = "Incorrect rate 1")
    @Min(5)
    @Max(500000)
    public BigDecimal rate1;

    @Required(message = "Incorrect rate 2")
    @Min(5)
    @Max(500000)
    public BigDecimal rate2;

    public String rateUnit;

    @Min(0)
    @Max(300)
    public BigDecimal benchTime = BigDecimal.ZERO;

    private BigDecimal calculateWorkDaysPerYear(){
        return hoursPerYear.divide(hoursPerDay,BigDecimal.ROUND_HALF_DOWN);
    }

    private BigDecimal calculateWorkDaysPerMonth() {
        return calculateHoursPerMonth()
            .divide(hoursPerDay,BigDecimal.ROUND_HALF_DOWN);
    }

    private BigDecimal calculateWorkDaysPerYearWithBenchTime() {
        return hoursPerYear.divide(hoursPerDay,BigDecimal.ROUND_HALF_DOWN).subtract(benchTime);
    }

    private BigDecimal calculateHoursPerYearWithBenchTime() {
        return hoursPerYear.subtract(benchTime.multiply(hoursPerDay));
    }
    
    private BigDecimal calculateHoursPerMonth() {
        return hoursPerYear
                .divide(new BigDecimal(12),BigDecimal.ROUND_HALF_DOWN);
                //.subtract(benchTime.multiply(hoursPerDay));
    }

    private BigDecimal calculateAnnualRate(BigDecimal rate) {
        if("hour".equalsIgnoreCase(rateUnit)){
            return rate.multiply(hoursPerYear);
        }  else {
            return rate.multiply(calculateWorkDaysPerYear());
        }
    }

    private BigDecimal calculateAnnualRateWithBenchTime(BigDecimal rate) {
        if("hour".equalsIgnoreCase(rateUnit)){
            return rate.multiply(calculateHoursPerYearWithBenchTime());
        }  else {
            return rate.multiply(calculateWorkDaysPerYearWithBenchTime());
        }
    }

    private BigDecimal calculateMonthRate(BigDecimal rate) {
        if("hour".equalsIgnoreCase(rateUnit)){
            return rate.multiply(calculateHoursPerMonth());
        }  else {
            return rate.multiply(calculateWorkDaysPerMonth());
        }
    }


    private BigDecimal calculateRate1Annually() {
        return calculateAnnualRate(rate1);
    }

    public String getRate1Annually() {
        return  getNumberFormat().format(calculateAnnualRate(rate1));
    }

    private BigDecimal calculateRate2Annually() {
        return  calculateAnnualRateWithBenchTime(rate2);
    }

    public String getRate2Annually() {
        return  getNumberFormat().format(calculateRate2Annually());
    }

    private BigDecimal calculateRateAnnuallyDifference() {
        return calculateRate2Annually().subtract(calculateRate1Annually());
    }

    public String getRateAnnuallyDifference() {
        return getNumberFormat().format(calculateRateAnnuallyDifference());
    }

    private NumberFormat getNumberFormat(){
        return NumberFormat.getInstance(Locale.UK);
    }

    public String getRateMonthlyDifference() {
        return getNumberFormat().format(calculateRateMonthlyDifference());
    }

    private BigDecimal calculateRateMonthlyDifference() {
        return calculateRate2Monthly().subtract(calculateRate1Monthly());
    }

    private BigDecimal calculateRate1Monthly() {
        return calculateMonthRate(rate1);
    }

    private BigDecimal calculateRate2Monthly() {
        return calculateMonthRate(rate2);
    }

    public String getRate1Monthly() {
        return  getNumberFormat().format(calculateRate1Monthly());
    }

    public String getRate2Monthy() {
        return  getNumberFormat().format(calculateRate2Monthly());
    }
}
