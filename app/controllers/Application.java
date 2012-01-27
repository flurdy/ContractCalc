package controllers;

import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import models.*;

public class Application extends Controller {
    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);

    public static void index() {
        render();
    }

    public static void rateRedirect() {
        enterRate();
    }

    public static void enterRate(){
        render("Application/calculateRate.html");
    }

    public static void contact(){
        render();
    }
    public static void hoursPerYear(){
        render();
    }

    public static void hoursPerYearRedirect(){
        hoursPerYear();
    }

    public static void resources(){
        render();
    }

    public static void rateDifference(){
        render();
    }

    public static void rateDifferenceRedirect(){
        rateDifference();
    }


    public static void calculateRate(
            @Required(message="Form missing") @Valid ContractingRate contractingRate
    ){
        if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            enterRate();
        }
        String hourlyRate = contractingRate.getHourlyRate();
        String dailyRate = contractingRate.getDailyRate();
        String yearIncome = contractingRate.getYearlyRateIncludingUtilisation();
        params.flash();
        render(hourlyRate,dailyRate,yearIncome);
    }



    public static void calculateHoursPerYear(
            @Required @Valid HoursPerYearCalculation hoursPerYearCalculation ){
        
        if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            hoursPerYear();
        }

        final int optimisticDaysPerYear = hoursPerYearCalculation.calculateWorkDaysWithoutBankHolidays();
        final BigDecimal optimisticHoursPerYear = hoursPerYearCalculation.calculateWorkHoursWithoutBankHolidays();
        final int optimisticWeeksPerYear = hoursPerYearCalculation.calculateWorkWeeksWithoutBankHolidays();
        final BigDecimal optimisticPercentOfYear = hoursPerYearCalculation.calculateYearlyPercentageWithoutBankHolidays();

        final int realisticDaysPerYear = hoursPerYearCalculation.calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin();
        final BigDecimal realisticHoursPerYear = hoursPerYearCalculation.calculateWorkHoursWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin();
        final int realisticWeeksPerYear = hoursPerYearCalculation.calculateWorkWeeksWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin();
        final BigDecimal realisticPercentOfYear =  hoursPerYearCalculation.calculateYearlyPercentageWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin();

        int pessimisticDaysPerYear = hoursPerYearCalculation.calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench();

        final BigDecimal pessimisticHoursPerYear = hoursPerYearCalculation.calculateWorkHoursWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench();
        final int pessimisticWeeksPerYear = hoursPerYearCalculation.calculateWorkWeeksWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNoSicWithBench();
        final BigDecimal pessimisticPercentOfYear =  hoursPerYearCalculation.calculateYearlyPercentageWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNoSicWithBench();

        params.flash();
        render("Application/hoursPerYear.html",
                optimisticWeeksPerYear,optimisticDaysPerYear,optimisticHoursPerYear,
                realisticWeeksPerYear,realisticDaysPerYear,realisticHoursPerYear,
                pessimisticWeeksPerYear,pessimisticDaysPerYear,pessimisticHoursPerYear,
                optimisticPercentOfYear,realisticPercentOfYear,pessimisticPercentOfYear);
    }



    public static void calculateRateDifference(
            @Required @Valid RateDifferenceCalculation rateDifferenceCalculation ){

        if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            Logger.info("hPY" + rateDifferenceCalculation.hoursPerYear);
            rateDifference();
        }

        String annualRate1 = rateDifferenceCalculation.getRate1Annually();
        String annualRate2 = rateDifferenceCalculation.getRate2Annually();
        String annualRateDifference = rateDifferenceCalculation.getRateAnnuallyDifference();
        String monthRate1 = rateDifferenceCalculation.getRate1Monthly();
        String monthRate2 = rateDifferenceCalculation.getRate2Monthy();
        String monthRateDifference = rateDifferenceCalculation.getRateMonthlyDifference();

        params.flash();
        render("Application/rateDifference.html",
                annualRate1, annualRate2, annualRateDifference,
                monthRate1, monthRate2, monthRateDifference);
    }


}