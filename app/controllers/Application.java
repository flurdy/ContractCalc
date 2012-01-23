package controllers;

import play.*;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

import models.*;

public class Application extends Controller {

    private static final int WORKDAYS_PER_YEAR = 260;

    public static void index() {
        render();
    }

    public static void rateRedirect() {
        enterRate();
    }

    public static void enterRate(){
         render("Application/calculateRate.html");
    }

    public static void calculateRate(
            @Required(message = "Salary required") @Min(1) BigDecimal salary,
            @Required(message = "Cost required")
            @Min(1) @Max(5) BigDecimal cost,
            @Required(message = "Yearly hours required")
            @Min(1) @Max(2500) BigDecimal hoursYear,
            @Required(message = "Daily hours required")
            @Min(1) @Max(24) BigDecimal hoursDay,
            @Required(message = "Utilisation required")
            @Min(0.1) @Max(3) BigDecimal utilisation,
            @Required(message = "Tax required")
            @Min(0.1) @Max(3) BigDecimal tax
    ){
       if(validation.hasErrors()) {
           params.flash();
           validation.keep();
           enterRate();
       }
        BigDecimal calculation = salary.multiply(cost).multiply(tax)
                .divide(hoursYear, RoundingMode.HALF_UP)
                .divide(utilisation, RoundingMode.HALF_UP);
        final NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
        String hourlyRate = numberFormat.format(Math.round(calculation.doubleValue()));
        BigDecimal dailyCalculation = new BigDecimal(hourlyRate).multiply(hoursDay);
        String dailyRate = numberFormat.format(Math.round(dailyCalculation.doubleValue()));
        BigDecimal yearCalculation = new BigDecimal(hourlyRate).multiply(hoursYear)
                .divide(utilisation, RoundingMode.HALF_UP);
        String yearIncome = numberFormat.format(Math.round(yearCalculation.doubleValue()));
        params.flash();
        render(hourlyRate,dailyRate,yearIncome);
    }

    public static void hoursPerYear(){
        render();
    }

    public static void hoursPerYearRedirect(){
        hoursPerYear();
    }

    public static void calculateHoursPerYear(
            @Required(message = "Daily hours required")
            @Min(1) @Max(24) Integer hoursPerDay,
            @Required(message = "Holidays required")
            @Min(0) @Max(260) Integer holidays,
            @Required(message = "Bank Holidays required")
            @Min(0) @Max(260) Integer bankHolidays,
            @Min(0) @Max(260) Integer trainingDays,
            @Min(0) @Max(260) Integer adminDays,
            @Required(message = "Sick days required")
            @Min(0) @Max(260) Integer sickDays,
            @Min(0) @Max(260) Integer benchDays ){
        
        if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            hoursPerYear();
        }

        final int optimisticDaysPerYear = WORKDAYS_PER_YEAR - bankHolidays;
        final int optimisticHoursPerYear = optimisticDaysPerYear * hoursPerDay;
        final int optimisticWeeksPerYear = optimisticDaysPerYear / 5;
        final BigDecimal optimisticPercentOfYear = calculateYearlyPercentage(optimisticDaysPerYear);

        int realisticDaysPerYear = optimisticDaysPerYear -holidays;
        if( trainingDays != null ){  realisticDaysPerYear -= trainingDays; }
        if( adminDays != null ){  realisticDaysPerYear -= adminDays; }
//        if( sickDays != null ){  realisticDaysPerYear -= sickDays; }

        final int realisticHoursPerYear = realisticDaysPerYear * hoursPerDay;
        final int realisticWeeksPerYear = realisticDaysPerYear / 5;
        final BigDecimal realisticPercentOfYear =  calculateYearlyPercentage(realisticDaysPerYear);

        int pessimisticDaysPerYear = realisticDaysPerYear;
        if( sickDays != null ){  pessimisticDaysPerYear -= sickDays; }
        if( benchDays != null ){  pessimisticDaysPerYear -= benchDays; }

        final int pessimisticHoursPerYear = pessimisticDaysPerYear * hoursPerDay;
        final int pessimisticWeeksPerYear = pessimisticDaysPerYear / 5;
        final BigDecimal pessimisticPercentOfYear =  calculateYearlyPercentage(pessimisticDaysPerYear);

        params.flash();
        render("Application/hoursPerYear.html",
                optimisticWeeksPerYear,optimisticDaysPerYear,optimisticHoursPerYear,
                realisticWeeksPerYear,realisticDaysPerYear,realisticHoursPerYear,
                pessimisticWeeksPerYear,pessimisticDaysPerYear,pessimisticHoursPerYear,
                optimisticPercentOfYear,realisticPercentOfYear,pessimisticPercentOfYear);
    }

    private static BigDecimal calculateYearlyPercentage(int daysPerYear){
        return new BigDecimal(daysPerYear)
                .divide(new BigDecimal(WORKDAYS_PER_YEAR), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
    }

}