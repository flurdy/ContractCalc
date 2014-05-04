package models;


import play.Logger;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ContractingRate  {

    @Required(message = "Salary required") @Min(1)
    public BigDecimal salary;

    @Required(message = "Cost required")
    @Min(0) @Max(500)
    public  BigDecimal cost;

    // @Required(message = "Risk required")
    @Min(0) @Max(500)
    public  BigDecimal risk;

    @Required(message = "Yearly hours required")
    @Min(1) @Max(2500)
    public BigDecimal hoursYear;

    @Required(message = "Utilisation required")
    @Min(0) @Max(100)
    public  BigDecimal utilisation;

    @Required(message = "Tax required")
    @Min(-100) @Max(300)
    public BigDecimal tax;

    public String dayOrHoursPerYearChoice;

    private BigDecimal contractRate;

    public static class ContractingRateBuilder{
        BigDecimal salary;
        BigDecimal cost;
        BigDecimal risk;
        BigDecimal hoursYear;
        BigDecimal utilisation;
        BigDecimal tax;
        public  ContractingRateBuilder salary(BigDecimal salary){
            this.salary = salary;
            return this;
        }
        public ContractingRateBuilder cost(BigDecimal cost){
            this.cost = cost;
            return this;
        }
        public ContractingRateBuilder risk(BigDecimal risk){
            this.risk = risk;
            return this;
        }
        public ContractingRateBuilder hoursYear(BigDecimal hoursYear ){
            this.hoursYear = hoursYear;
            return this;
        }
        public ContractingRateBuilder utilisation(BigDecimal utilisation){
            this.utilisation = utilisation;
            return this;
        }
        public ContractingRateBuilder tax(BigDecimal tax){
            this.tax = tax;
            return this;
        }

        public ContractingRate build(){
           return new ContractingRate(this);
        }
    }

    public ContractingRate(BigDecimal salary, BigDecimal cost,  BigDecimal risk, BigDecimal hoursYear,   BigDecimal utilisation, BigDecimal tax) {
        this.salary = salary;
        this.cost = cost;
        this.risk = risk;
        this.hoursYear = hoursYear;
        this.utilisation = utilisation;
        this.tax = tax;
        this.contractRate = calculateRate();
    }

    private ContractingRate(ContractingRateBuilder builder) {
        this.salary = builder.salary;
        this.cost = builder.cost;
        this.risk = builder.risk;
        this.hoursYear = builder.hoursYear;
        this.utilisation = builder.utilisation;
        this.tax = builder.tax;
        this.contractRate = calculateRate();
    }

    private BigDecimal calculateRate(){
//            Logger.info("#################");
        final BigDecimal annualRate = calculateAnnualTarget();
        final BigDecimal reducedHours = reduceHourDueToUtilisation();
        final BigDecimal rateByHoursPerYear =  annualRate.divide(reducedHours, RoundingMode.HALF_UP);
//            Logger.info("Rate per hour per year is " + rateByHoursPerYear);
        final BigDecimal roundedRate = rateByHoursPerYear.setScale(0, RoundingMode.HALF_UP);
//            Logger.info("Rounded hourly rate is " + roundedRate);
        return roundedRate;
    }

    public String getHourlyRate(){
        final BigDecimal hourlyRate = getContractRate();
//        Logger.info("hourly calc is " + hourlyRate.doubleValue());
        return getNumberFormat().format(hourlyRate.doubleValue());
    }

    public String getDailyRate(int hoursPerDay){
        return getDailyRate(new BigDecimal(hoursPerDay));
    }

    public String getDailyRate(BigDecimal hoursPerDay){
        final BigDecimal dailyCalculation = calculateDailyRate(hoursPerDay);
//        Logger.info("daily calc is " + dailyCalculation);

        final BigDecimal rounded1 = dailyCalculation.setScale(0, RoundingMode.HALF_UP);
//        Logger.info("rounded1 is " + rounded1);
        final BigDecimal rounded2 = rounded1.multiply(new BigDecimal(2));
//        Logger.info("rounded2 is " + rounded2);
        final BigDecimal rounded3 = rounded2.setScale(-1, RoundingMode.HALF_UP);
//        Logger.info("rounded3 is " + rounded3);
        final BigDecimal rounded35 = rounded3.setScale(0, RoundingMode.HALF_UP);
//        Logger.info("rounded35 is " + rounded35);
        final BigDecimal rounded4 = rounded35.divide(new BigDecimal(2), RoundingMode.HALF_UP);
//        Logger.info("rounded4 is " + rounded4);
        final BigDecimal rounded5 = rounded4.setScale(0, RoundingMode.HALF_UP);
//        Logger.info("rounded5 is " + rounded5);

        return getNumberFormat().format(rounded5.doubleValue());
    }

    public String getYearlyIncome(){
        if( utilisation.equals(BigDecimal.ZERO)) {
            return "0";
        } else {
            final BigDecimal calculation = calculateAnnualTarget();
            final BigDecimal reduced = reduceDueToUtilisation(calculation);
            final BigDecimal rounded = reduced.setScale(-2, RoundingMode.HALF_UP);
            return getNumberFormat().format(rounded.doubleValue());
        }
    }

    private BigDecimal getContractRate(){
        if(contractRate==null){
            contractRate = calculateRate();
        }
        return contractRate;
    }

    private BigDecimal calculateAnnualTarget(){
//            Logger.info("Cost is " + cost);
//            Logger.info("Tax is " + tax);
//            Logger.info("Salary is " + salary);
        final BigDecimal costInflation =  increaseByPercentage(salary,cost);
//            Logger.info("Salary + cost is " + costInflation);
        final BigDecimal taxInflation =  increaseByPercentage(costInflation,tax);
//            Logger.info("Salary + cost + tax is " + taxInflation);
        final BigDecimal riskInflation =  increaseByPercentage(taxInflation,risk);
        return riskInflation;
    }

    public BigDecimal calculateDailyRate(BigDecimal hoursPerDay){
        final BigDecimal annualRate = calculateAnnualTarget();
//            Logger.info("annual rate is " + annualRate);
        final BigDecimal reducedHours = reduceHourDueToUtilisation();
//            Logger.info("reducedHours is " + reducedHours);
        final BigDecimal daysPerYear = reducedHours.divide(hoursPerDay, RoundingMode.HALF_UP);
//            Logger.info("daysPerYear is " + daysPerYear);
        final BigDecimal dailyCalculation = annualRate.divide(daysPerYear, RoundingMode.HALF_UP);
        // contractRate.multiply(hoursDay).setScale(0, RoundingMode.HALF_UP);
//            Logger.info("dailyCalculation is " + dailyCalculation);
        final BigDecimal dailyRounded = dailyCalculation.setScale(0, RoundingMode.HALF_UP);
//         Logger.info("dailyRounded is " + dailyRounded);
        return dailyRounded;
    }

    private BigDecimal reduceHourDueToUtilisation(){
            Logger.info("hours was " + hoursYear);
        return reduceDueToUtilisation(hoursYear);
    }

    private BigDecimal reduceDueToUtilisation(BigDecimal base){
//            Logger.info("Utilisation is " + utilisation);
        final BigDecimal reduced = adjustByPercentage(base, utilisation);
//            Logger.info("utlisation reduce is " + reduced);
        return reduced;
    }

    private static BigDecimal toPercentage(BigDecimal percentage){
        if( percentage.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        } else {
            return percentage.divide(new BigDecimal(100),5,RoundingMode.HALF_UP);
        }
    }

    private static BigDecimal adjustByPercentage(BigDecimal base, BigDecimal percentage){
        if( percentage.equals(BigDecimal.ZERO)) {
            return base;
        } else {
//            Logger.info("percentage is "+percentage);
            return base.multiply(toPercentage(percentage));
        }
    }

    private static BigDecimal increaseByPercentage(BigDecimal base, BigDecimal percentage){
         if( percentage.equals(BigDecimal.ZERO)) {
            return base;
        } else {
            return base.multiply(BigDecimal.ONE.add(percentage.divide(new BigDecimal(100),5,RoundingMode.HALF_UP)));
        }
    }

    private static BigDecimal decreaseByPercentage(BigDecimal base, BigDecimal percentage){
        if( percentage.equals(BigDecimal.ZERO)) {
           return base;
        } else {
           return base.divide(toPercentage(percentage),RoundingMode.HALF_UP);
       }
    }

    private static BigDecimal invertByPercentage(BigDecimal percentage){
        return new BigDecimal(100).subtract(percentage);
    }

    private NumberFormat getNumberFormat(){
        return NumberFormat.getInstance(Locale.UK);
    }
}
