package de.jgh.pricetrend.mailparser;

public enum PercentileEnum {
    FIVE(5),
    TEN(10),
    TWENTY_FIVE(25),
    THIRTY(30),
    FIFTY(50),
    SEVENTY_FIVE(75),
    EIGHTY(80),
    NINETY(90),
    NINETY_FIVE(95),
    MAX(-1);

    private final int percentile;
    private PercentileEnum(int percentile) {
        this.percentile = percentile;
    }

    public int getPercentile() {
        return this.percentile;
    }
}
