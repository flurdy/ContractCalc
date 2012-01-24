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
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("44",contractingRate.getHourlyRate());
    }

    @Test
    public void noUtilisation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(0))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("0",contractingRate.getYearlyRateIncludingUtilisation());
    }
    @Test
    public void fullUtilisation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(100))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("66,000",contractingRate.getYearlyRateIncludingUtilisation());
    }

    @Test
    public void aBitLessTax() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(-10))
                        .build();


        assertEquals("288",contractingRate.getDailyRate());
    }

    @Test
    public void noCost() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(0))
                        .hoursYear(new BigDecimal(1500))
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("296",contractingRate.getDailyRate());
    }

    @Test
    public void happyDayRateCalculation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("352",contractingRate.getDailyRate());
    }

    @Test
    public void happyYearCalculation() {

        ContractingRate contractingRate =
                new ContractingRate.ContractingRateBuilder()
                        .salary(new BigDecimal(50000))
                        .cost(new BigDecimal(20))
                        .hoursYear(new BigDecimal(1500))
                        .hoursDay(new BigDecimal(8))
                        .utilisation(new BigDecimal(90))
                        .tax(new BigDecimal(10))
                        .build();


        assertEquals("59,400", contractingRate.getYearlyRateIncludingUtilisation());
    }

    
}
