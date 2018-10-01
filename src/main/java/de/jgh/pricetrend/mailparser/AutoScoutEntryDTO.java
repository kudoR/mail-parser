package de.jgh.pricetrend.mailparser;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class AutoScoutEntryDTO {
    private String title;
    @JsonIgnore
    private String link;
    private String preis;
    private String laufleistung;
    private String erstzulassung;
    private String motorleistung;
    private LocalDate insertDate;
    private LocalDate receivedDate;

    public AutoScoutEntryDTO(String title, String link, String preis, String laufleistung, String erstzulassung, String motorleistung, LocalDate receivedDate) {
        this.title = title;
        this.link = link;
        this.preis = preis;
        this.laufleistung = laufleistung;
        this.erstzulassung = erstzulassung;
        this.motorleistung = motorleistung;
        this.receivedDate = receivedDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public LocalDate getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(LocalDate insertDate) {
        this.insertDate = insertDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPreis() {
        return preis;
    }

    public void setPreis(String preis) {
        this.preis = preis;
    }

    public String getLaufleistung() {
        return laufleistung;
    }

    public void setLaufleistung(String laufleistung) {
        this.laufleistung = laufleistung;
    }

    public String getErstzulassung() {
        return erstzulassung;
    }

    public void setErstzulassung(String erstzulassung) {
        this.erstzulassung = erstzulassung;
    }

    public String getMotorleistung() {
        return motorleistung;
    }

    public void setMotorleistung(String motorleistung) {
        this.motorleistung = motorleistung;
    }
}
