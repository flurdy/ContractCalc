import models.ContractingRate;
import org.junit.Test;
import play.test.UnitTest;

import java.math.BigDecimal;

public class RateTest extends UnitTest {


    @Test
    public void happyHourlyRateCalculation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("49",contractingRate.getHourlyRate());
    }

    @Test
    public void noUtilisation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(0))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("0",contractingRate.getYearlyIncome());
    }

    @Test
    public void fullUtilisation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("66,000",contractingRate.getYearlyIncome());
    }

    @Test
    public void aBitLessTaxPerHour() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(-10))
                        .build();


        assertEquals("40",contractingRate.getHourlyRate());
    }

    @Test
    public void aBitLessTaxPerDay() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(-10))
                        .build();


        assertEquals("320",contractingRate.getDailyRate(8));
    }

    @Test
    public void noCost() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("295",contractingRate.getDailyRate(8));
    }

    @Test
    public void happyDayRateCalculation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("390",contractingRate.getDailyRate(8));
    }

    @Test
    public void happyYearCalculation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("59,400", contractingRate.getYearlyIncome());
    }


    @Test
    public void an8HourDay() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(1600))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();


        assertEquals("250",contractingRate.getDailyRate(8));
    }


    @Test
    public void a7HourDay() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(1400))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();


        assertEquals("250",contractingRate.getDailyRate(7));
    }

    @Test
    public void a2HourDay() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(400))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();

        assertEquals("250",contractingRate.getDailyRate(2));
    }

    @Test
    public void rounded2to0Rate() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(48700))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(400))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();

        assertEquals("122",contractingRate.getHourlyRate());
        assertEquals("120",contractingRate.getDailyRate(1));
    }



    @Test
    public void rounded3to5Rate() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(49000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(400))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();

        assertEquals("123",contractingRate.getHourlyRate());
        assertEquals("125",contractingRate.getDailyRate(1));
    }




    @Test
    public void rounded5to5Rate() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(400))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();

        assertEquals("125",contractingRate.getHourlyRate());
        assertEquals("125",contractingRate.getDailyRate(1));
    }



    @Test
    public void rounded8to0Rate() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(10))
                        .hoursYear(new BigDecimal(400))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(0))
                        .build();

        assertEquals("138",contractingRate.getHourlyRate());
        assertEquals("140",contractingRate.getDailyRate(1));
    }

}
