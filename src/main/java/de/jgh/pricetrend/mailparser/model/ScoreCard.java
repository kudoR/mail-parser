package de.jgh.pricetrend.mailparser.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScoreCard {
    @Id
    private Long id;

    private Integer pricePercentile;
    private Integer registrationPercentile;
    private Integer mileagePercentile;

    public ScoreCard() {
    }

    public ScoreCard(Long id, Integer pricePercentile, Integer registrationPercentile, Integer mileagePercentile) {
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

    public Integer getPricePercentile() {
        return pricePercentile;
    }

    public void setPricePercentile(Integer pricePercentile) {
        this.pricePercentile = pricePercentile;
    }

    public Integer getRegistrationPercentile() {
        return registrationPercentile;
    }

    public void setRegistrationPercentile(Integer registrationPercentile) {
        this.registrationPercentile = registrationPercentile;
    }

    public Integer getMileagePercentile() {
        return mileagePercentile;
    }

    public void setMileagePercentile(Integer mileagePercentile) {
        this.mileagePercentile = mileagePercentile;
    }
}
