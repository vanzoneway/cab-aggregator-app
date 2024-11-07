package com.modsen.ridesservice.service.component;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class RideServicePriceGeneratorTest {

    private final RideServicePriceGenerator generator = new RideServicePriceGenerator();

    @Test
    void testGenerateRandomCostIsWithinRange() {
        for (int i = 0; i < 100; i++) {
            BigDecimal cost = generator.generateRandomCost();
            assertThat(cost)
                    .isGreaterThanOrEqualTo(BigDecimal.ZERO)
                    .isLessThanOrEqualTo(new BigDecimal("99999.99"));
        }
    }

    @Test
    void testGenerateRandomCostHasCorrectScale() {
        for (int i = 0; i < 100; i++) {
            BigDecimal cost = generator.generateRandomCost();
            assertThat(cost.scale()).isEqualTo(2);
            assertThat(cost).isEqualTo(cost.setScale(2, RoundingMode.HALF_UP));
        }
    }

    @Test
    void testGenerateRandomCostFormat() {
        for (int i = 0; i < 100; i++) {
            BigDecimal cost = generator.generateRandomCost();
            String costAsString = cost.toString();
            assertThat(costAsString).matches("\\d{1,5}\\.\\d{2}");
        }
    }

}
