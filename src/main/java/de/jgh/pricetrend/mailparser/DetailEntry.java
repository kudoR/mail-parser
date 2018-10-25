package de.jgh.pricetrend.mailparser;

import javax.persistence.*;

@Entity
public class DetailEntry {
    @EmbeddedId
    private DetailEntryId id;

    private Double price;
    private boolean processed;
    private String model;
    private String anbieterType;
    private String city;
    private String country;

    public DetailEntry() {
    }

    public DetailEntry(DetailEntryId id) {
        this.id = id;
    }

    public String getAnbieterType() {
        return anbieterType;
    }

    public void setAnbieterType(String anbieterType) {
        this.anbieterType = anbieterType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public DetailEntryId getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
