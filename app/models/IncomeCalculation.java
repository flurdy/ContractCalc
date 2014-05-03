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


public class IncomeCalculation {

    @Required(message = "Rate desired required") @Min(1)
    public BigDecimal rate;

    @Required(message = "Charge unit per year required")
    @Min(0) @Max(3000)
    public BigDecimal peryear;

	 @Required(message = "Rate length required")
    public String ratelength;

    @Required(message = "Cost required")
    @Min(0) @Max(300)
    public BigDecimal cost;

    @Required(message = "Utilisation required")
    @Min(0) @Max(100)
    public BigDecimal utilisation;


    private static final BigDecimal hundred = new BigDecimal(100);

    private BigDecimal grossIncome;
    private BigDecimal realisticIncome;
    private BigDecimal comparableIncome;



    public IncomeCalculation(BigDecimal rate, BigDecimal peryear, String ratelength, BigDecimal cost, BigDecimal utilisation){
    	this.rate = rate;
    	this.peryear = peryear;
    	this.ratelength = ratelength;
    	this.cost = cost;
    	this.utilisation = utilisation;

    }

    public void calculateIncome(){

    	this.grossIncome = rate.multiply( peryear );

      Logger.info("################# grossIncome"+grossIncome);
    	final BigDecimal utilisationPercentage = calculatePercentageDecrease(utilisation);

    	this.realisticIncome = grossIncome.multiply(utilisationPercentage);
      Logger.info("################# realisticIncome"+realisticIncome);

    	final BigDecimal costPercentage = calculatePercentageIncrease(cost);
    	this.comparableIncome = this.realisticIncome
    		.multiply(costPercentage);
      Logger.info("################# comparableIncome"+comparableIncome);
    }

    private BigDecimal calculatePercentageIncrease(BigDecimal percentage){
    	if(percentage == null || percentage.equals(BigDecimal.ZERO)){
    		return BigDecimal.ONE;
    	} else {
    		final BigDecimal minorPercentage = percentage
    			.setScale(2,RoundingMode.HALF_UP)
    			.divide(new BigDecimal(100),RoundingMode.HALF_UP);
      	Logger.info("################# inc minorPercentage "+minorPercentage);
    		return BigDecimal.ONE.subtract( minorPercentage );
    	}
    }


    private BigDecimal calculatePercentageDecrease(BigDecimal percentage){
    	if(percentage == null || percentage.equals(hundred)){
    		return BigDecimal.ONE;
    	} else {

    		final BigDecimal minorPercentage = percentage
    			.setScale(2,RoundingMode.HALF_UP)
    			.divide(new BigDecimal(100),RoundingMode.HALF_UP);
      	Logger.info("################# decr minorPercentage "+minorPercentage);

    		final BigDecimal invertedPercentage = BigDecimal.ONE.subtract(minorPercentage);
      	Logger.info("################# invertedPercentage "+invertedPercentage);
    		//return invertedPercentage;
    		if( percentage.compareTo(hundred) > 0 ){
    			return BigDecimal.ONE.add( invertedPercentage );
    		} else {
    			return BigDecimal.ONE.subtract( invertedPercentage );
    		}
    	}
    }

    private BigDecimal calculatePrecision(BigDecimal value){
    	if( value.precision() <= 4 ){
    		return value;
    	} else {
    		final int scale = 4 - value.precision() + value.scale();
    		return value.setScale( scale, RoundingMode.HALF_UP);
    	}
    }



    public String getGrossIncome(){
    	return getIncome( grossIncome);
    }


    public String getRealisticIncome(){
    	return getIncome(realisticIncome);
    }

    public String getComparableIncome(){
    	return getIncome(comparableIncome);
    }

    private String getIncome(BigDecimal income){
    	if(income == null || income.equals(BigDecimal.ZERO)) {
    		return "N/A";
    	} else {
    		final double incomeValue = calculatePrecision( income ) .doubleValue();
    		return getNumberFormat().format(incomeValue);
    	}
    }

    public String getRawGrossIncome(){
    	return getRawIncome( grossIncome);
    }


    public String getRawRealisticIncome(){
    	return getRawIncome(realisticIncome);
    }

    public String getRawComparableIncome(){
    	return getRawIncome(comparableIncome);
    }

    private String getRawIncome(BigDecimal income){
    	if(income == null || income.equals(BigDecimal.ZERO)) {
    		return "N/A";
    	} else {
    		final double incomeValue = income.doubleValue();
    		return getNumberFormat().format(incomeValue);
    	}
    }


    private NumberFormat getNumberFormat(){
        return NumberFormat.getInstance(Locale.UK);
    }



}
