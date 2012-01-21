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

    public static void index() {
        render();
    }
    
    public static void calculate(
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
           index();
       }
        BigDecimal calculation = salary.multiply(cost).divide(hoursYear, RoundingMode.HALF_UP)
                    .multiply(utilisation).multiply(tax);
        final NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
        String hourlyRate = numberFormat.format(Math.round(calculation.doubleValue()));
        BigDecimal dailyCalculation = new BigDecimal(hourlyRate).multiply(hoursDay);
        String dailyRate = numberFormat.format(Math.round(dailyCalculation.doubleValue()));
        BigDecimal yearCalculation = new BigDecimal(hourlyRate).multiply(hoursYear);
        String yearIncome = numberFormat.format(Math.round(yearCalculation.doubleValue()));
        params.flash();
        render("Application/index.html",hourlyRate,dailyRate,yearIncome);
    }

}