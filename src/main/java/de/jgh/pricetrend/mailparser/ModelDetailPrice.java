package de.jgh.pricetrend.mailparser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "model_detail_price")
public class ModelDetailPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate erstzulassung;
    private Long laufleistung;
    private BigDecimal preis;
    private String model;

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

    public Long getId() {
        return id;
    }
}
