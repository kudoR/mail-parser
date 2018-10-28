package de.jgh.pricetrend.mailparser.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScoreCard {
    @Id
    private Long id;

    private Double pricePercentile;
    private Double registrationPercentile;
    private Double mileagePercentile;

    public ScoreCard() {
    }

    public ScoreCard(Long id, Double pricePercentile, Double registrationPercentile, Double mileagePercentile) {
        this.id = id;
        this.pricePercentile = pricePercentile;
        this.registrationPercentile = registrationPercentile;
        this.mileagePercentile = mileagePercentile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPricePercentile() {
        return pricePercentile;
    }

    public void setPricePercentile(Double pricePercentile) {
        this.pricePercentile = pricePercentile;
    }

    public Double getRegistrationPercentile() {
        return registrationPercentile;
    }

    public void setRegistrationPercentile(Double registrationPercentile) {
        this.registrationPercentile = registrationPercentile;
    }

    public Double getMileagePercentile() {
        return mileagePercentile;
    }

    public void setMileagePercentile(Double mileagePercentile) {
        this.mileagePercentile = mileagePercentile;
    }

}
