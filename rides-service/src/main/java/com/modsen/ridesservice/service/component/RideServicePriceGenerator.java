package com.modsen.ridesservice.service.component;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Component
public class RideServicePriceGenerator {

    private static final int SCALE = 2;
    private static final BigDecimal MAX_VALUE = new BigDecimal("99999.99");

    public BigDecimal generateRandomCost() {
        Random random = new Random();
        int integerPart = random.nextInt(MAX_VALUE.intValue() + 1);
        int fractionalPart = random.nextInt(100);
        BigDecimal randomCost = new BigDecimal(integerPart + "." + String.format("%02d", fractionalPart));
        return randomCost.setScale(SCALE, RoundingMode.HALF_UP);
    }

}
