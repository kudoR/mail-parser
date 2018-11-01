package de.jgh.pricetrend.mailparser.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class ScoreCardExtended {
    @Id
    private Long id;

    private Integer pricePercentile;
    private Integer registrationPercentile;
    private Integer mileagePercentile;
    private LocalDate erstzulassung;
    private Long laufleistung;
    private BigDecimal preis;
    private String model;

    public ScoreCardExtended() {
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

    public LocalDate getErstzulassung() {
        return erstzulassung;
    }

    public void setErstzulassung(LocalDate erstzulassung) {
        this.erstzulassung = erstzulassung;
    }

    public Long getLaufleistung() {
        return laufleistung;
    }

    public void setLaufleistung(Long laufleistung) {
        this.laufleistung = laufleistung;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
