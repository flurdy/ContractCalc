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
    @Min(0) @Max(300)
    public  BigDecimal cost;
    
    @Required(message = "Yearly hours required")
    @Min(1) @Max(2500)
    public BigDecimal hoursYear;
    
    @Required(message = "Daily hours required")
    @Min(1) @Max(24)
    public BigDecimal hoursDay;
    
    @Required(message = "Utilisation required")
    @Min(0) @Max(100)
    public  BigDecimal utilisation;
    
    @Required(message = "Tax required")
    @Min(-100) @Max(300)
    public BigDecimal tax;


    private BigDecimal contractRate;

    public static class ContractingRateBuilder{
        BigDecimal salary;
        BigDecimal cost;
        BigDecimal hoursYear;
        BigDecimal hoursDay;
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
        public ContractingRateBuilder hoursYear(BigDecimal hoursYear ){
            this.hoursYear = hoursYear;
            return this;
        }
        public ContractingRateBuilder hoursDay(BigDecimal hoursDay){
            this.hoursDay = hoursDay;
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

    public ContractingRate(BigDecimal salary, BigDecimal cost, BigDecimal hoursYear, BigDecimal hoursDay, BigDecimal utilisation, BigDecimal tax) {
        this.salary = salary;
        this.cost = cost;
        this.hoursYear = hoursYear;
        this.hoursDay = hoursDay;
        this.utilisation = utilisation;
        this.tax = tax;
        this.contractRate = calculateRate();
    }

    private ContractingRate(ContractingRateBuilder builder) {
        this.salary = builder.salary;
        this.cost = builder.cost;
        this.hoursYear = builder.hoursYear;
        this.hoursDay = builder.hoursDay;
        this.utilisation = builder.utilisation;
        this.tax = builder.tax;
        this.contractRate = calculateRate();
    }

    private BigDecimal calculateRate(){
        Logger.info("Salary is " + salary);
        Logger.info("cost is " + cost);
        BigDecimal calculation =  increaseByPercentage(salary,cost);
        Logger.info("cost calc is " + calculation);
        Logger.info("tax is " + tax);
        calculation =  increaseByPercentage(calculation,tax);
        Logger.info("tax calc is " + calculation);
        calculation =  calculation.divide(hoursYear, RoundingMode.HALF_UP);
        Logger.info("year hours calc is " + calculation);
        Logger.info("Rate is " + calculation);
        return calculation.setScale(0, RoundingMode.HALF_UP);
    }

    public String getHourlyRate(){
        if(contractRate==null){
                contractRate = calculateRate();
        }
        Logger.info("hourly calc is " + contractRate.doubleValue());
        return getNumberFormat().format(contractRate.doubleValue());
    }

    public String getDailyRate(){
        if(contractRate==null){
            contractRate = calculateRate();
        }
        BigDecimal dailyCalculation = contractRate.multiply(hoursDay).setScale(0, RoundingMode.HALF_UP);
        Logger.info("daily calc is " + dailyCalculation);
        return getNumberFormat().format(dailyCalculation.doubleValue());
    }

    public String getYearlyRateIncludingUtilisation(){
        if(contractRate==null){
            contractRate = calculateRate();
        }
        BigDecimal calculation = contractRate.multiply(hoursYear);
        calculation = adjustByPercentage(calculation,utilisation);
        return getNumberFormat().format(calculation.doubleValue());
    }

    private static BigDecimal adjustByPercentage(BigDecimal base, BigDecimal percentage){
        return base.multiply(percentage.divide(new BigDecimal(100),5,RoundingMode.HALF_UP));
    }

    private static BigDecimal increaseByPercentage(BigDecimal base, BigDecimal percentage){
        return base.multiply(BigDecimal.ONE.add(percentage.divide(new BigDecimal(100),5,RoundingMode.HALF_UP)));
    }

    private NumberFormat getNumberFormat(){
        return NumberFormat.getInstance(Locale.UK);
    }
}
