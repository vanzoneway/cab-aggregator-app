package com.modsen.ridesservice.service.component;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RideServicePriceGeneratorTest {

    private final RideServicePriceGenerator generator = new RideServicePriceGenerator();

    @Test
    void testGenerateRandomCostIsWithinRange() {
        for (int i = 0; i < 100; i++) {
            BigDecimal cost = generator.generateRandomCost();
            assertTrue(cost.compareTo(BigDecimal.ZERO) >= 0, "Cost should be non-negative");
            assertTrue(cost.compareTo(new BigDecimal("99999.99")) <= 0, "Cost should not exceed 99999.99");
        }
    }

    @Test
    void testGenerateRandomCostHasCorrectScale() {
        for (int i = 0; i < 100; i++) {
            BigDecimal cost = generator.generateRandomCost();
            assertEquals(2, cost.scale(),
                    "Cost should have a scale of 2");
            assertEquals(cost, cost.setScale(2, RoundingMode.HALF_UP),
                    "Cost should be rounded correctly");
        }
    }

    @Test
    void testGenerateRandomCostFormat() {
        for (int i = 0; i < 100; i++) {
            BigDecimal cost = generator.generateRandomCost();
            String costAsString = cost.toString();
            assertTrue(costAsString.matches("\\d{1,5}\\.\\d{2}"),
                    "Cost should be in the format 'xxxxx.xx'");
        }
    }

}
