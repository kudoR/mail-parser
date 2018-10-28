package de.jgh.pricetrend.mailparser.model;

import de.jgh.pricetrend.mailparser.AnbieterType;
import de.jgh.pricetrend.mailparser.Artikelzustand;

import javax.persistence.*;

@Entity
public class DetailEntry {
    @EmbeddedId
    private DetailEntryId id;

    private Double price;
    private boolean processed;
    private String model;
    private AnbieterType anbieterType;
    private String zip;
    private String city;
    private Artikelzustand artikelzustand;
    private boolean unfall;

    public DetailEntry() {
    }

    public DetailEntry(DetailEntryId id) {
        this.id = id;
    }


    public AnbieterType getAnbieterType() {
        return anbieterType;
    }

    public void setAnbieterType(AnbieterType anbieterType) {
        this.anbieterType = anbieterType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public Artikelzustand getArtikelzustand() {
        return artikelzustand;
    }

    public void setArtikelzustand(Artikelzustand artikelzustand) {
        this.artikelzustand = artikelzustand;
    }

    public boolean isUnfall() {
        return unfall;
    }

    public void setUnfall(boolean unfall) {
        this.unfall = unfall;
    }
}
