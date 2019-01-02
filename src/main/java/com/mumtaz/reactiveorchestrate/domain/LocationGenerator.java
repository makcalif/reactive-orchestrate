package com.mumtaz.reactiveorchestrate.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

public class LocationGenerator {
    private static final MathContext mathContext = new MathContext(8);

    private static final Random random = new Random();


    private final BigDecimal longitude;

    private final BigDecimal latitude;


    public LocationGenerator(double longitude, double latitude) {
        this.longitude = new BigDecimal(longitude, mathContext);
        this.latitude = new BigDecimal(latitude, mathContext);
    }


    public Location location() {
        return new Location(longitude.add(randomDeviation(), mathContext),
                latitude.add(randomDeviation(), mathContext));
    }

    private BigDecimal randomDeviation() {
        return new BigDecimal((double) random.nextLong() % 100 / 1000000, mathContext);
    }
}
