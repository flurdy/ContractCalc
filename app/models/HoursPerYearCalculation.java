package models;

import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HoursPerYearCalculation {

    public static final int WORKDAYS_PER_YEAR = 260;

    @Required(message = "Daily hours required")
    @Min(1) @Max(24) 
    public BigDecimal hoursPerDay;
    
    @Required(message = "Holidays required")
    @Min(0) @Max(260)
    public Integer holidays;

    @Required(message = "Bank Holidays required")
    @Min(0) @Max(260)
    public Integer bankHolidays;

    @Min(0) @Max(260)
    public Integer trainingDays;

    @Min(0) @Max(260)
    public Integer adminDays;

    @Required(message = "Sick days required")
    @Min(0) @Max(260)
    public Integer sickDays;

    @Min(0) @Max(260)
    public Integer benchDays;

    public int calculateWorkDaysWithoutBankHolidays() {
        return WORKDAYS_PER_YEAR - bankHolidays;
    }

    public int calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin() {
        int workDays = calculateWorkDaysWithoutBankHolidays() - holidays;
        if( trainingDays != null ){  workDays -= trainingDays; }
        if( adminDays != null ){  workDays -= adminDays; }
//        if( sickDays != null ){  realisticDaysPerYear -= sickDays; }
        return workDays;
    }

    public int calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench() {
        int workDays = calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin();
        if( sickDays != null ){  workDays -= sickDays; }
        if( benchDays != null ){  workDays -= benchDays; }
        return workDays;
    }

    public BigDecimal calculateWorkHours(int workDays) {
        return hoursPerDay.multiply(new BigDecimal(workDays));
    }

    public BigDecimal calculateWorkHoursWithoutBankHolidays() {
        return calculateWorkHours(calculateWorkDaysWithoutBankHolidays()) ;
    }

    public BigDecimal calculateWorkHoursWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin() {
        return calculateWorkHours(calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin());
    }

    public BigDecimal calculateWorkHoursWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench() {
        return calculateWorkHours(calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench());
    }

    public int calculateWorkWeeksWithoutBankHolidays() {
        return calculateWorkDaysWithoutBankHolidays() / 5;
    }

    public int calculateWorkWeeksWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin() {
        return calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin() / 5;
    }

    public int calculateWorkWeeksWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNoSicWithBench() {
        return calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench() / 5;
    }

    private BigDecimal calculateYearlyPercentage(int daysWorked){
        return new BigDecimal(daysWorked)
                .divide(new BigDecimal(WORKDAYS_PER_YEAR), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateYearlyPercentageWithoutBankHolidays(){
        return calculateYearlyPercentage(calculateWorkDaysWithoutBankHolidays());
    }

    public BigDecimal calculateYearlyPercentageWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin(){
        return calculateYearlyPercentage(calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdmin());
    }

    public BigDecimal calculateYearlyPercentageWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNoSicWithBench(){
        return calculateYearlyPercentage(calculateWorkDaysWithoutBankHolidaysNorHolidaysNorTrainingNorAdminNorSicWithBench());
    }


}
