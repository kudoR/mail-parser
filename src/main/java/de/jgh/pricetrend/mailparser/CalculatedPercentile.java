package de.jgh.pricetrend.mailparser;

import java.time.LocalDateTime;

public class CalculatedPercentile {
    private Double doubleValue;
    private LocalDateTime localDateValue;
    private PercentileEnum label;
    private PercentileType percentileType;

    public CalculatedPercentile(PercentileType percentileType, double doubleValue, PercentileEnum label) {
        this.percentileType = percentileType;
        this.doubleValue = doubleValue;
        this.label = label;
    }

    public CalculatedPercentile(PercentileType percentileType, LocalDateTime localDateValue, PercentileEnum label) {
        this.percentileType = percentileType;
        this.localDateValue = localDateValue;
        this.label = label;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public PercentileEnum getLabel() {
        return label;
    }

    public void setLabel(PercentileEnum label) {
        this.label = label;
    }

    public LocalDateTime getLocalDateValue() {
        return localDateValue;
    }

    public void setLocalDateValue(LocalDateTime localDateValue) {
        this.localDateValue = localDateValue;
    }

    public PercentileType getPercentileType() {
        return percentileType;
    }

    public void setPercentileType(PercentileType percentileType) {
        this.percentileType = percentileType;
    }
}
