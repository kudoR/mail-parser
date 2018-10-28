package de.jgh.pricetrend.mailparser.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScoreCard {
    @Id
    private Long id;

    private Double evaluatedPriceScore;
    private Double givenPriceScore;

    public ScoreCard() {
    }

    public ScoreCard(Long id, Double evaluatedPriceScore, Double givenPriceScore) {
        this.id = id;
        this.evaluatedPriceScore = evaluatedPriceScore;
        this.givenPriceScore = givenPriceScore;
    }

    public Double getEvaluatedPriceScore() {
        return evaluatedPriceScore;
    }

    public void setEvaluatedPriceScore(Double evaluatedPriceScore) {
        this.evaluatedPriceScore = evaluatedPriceScore;
    }

    public Double getGivenPriceScore() {
        return givenPriceScore;
    }

    public void setGivenPriceScore(Double givenPriceScore) {
        this.givenPriceScore = givenPriceScore;
    }
}
