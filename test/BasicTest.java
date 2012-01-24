import controllers.Application;
import org.junit.*;

import java.math.BigDecimal;
import java.util.*;

import play.data.validation.Validation;
import play.test.*;
import models.*;

import models.ContractingRate;
public class BasicTest extends UnitTest {

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

}
