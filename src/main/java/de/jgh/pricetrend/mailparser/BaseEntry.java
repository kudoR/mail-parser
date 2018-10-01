package de.jgh.pricetrend.mailparser;

import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class BaseEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String link;
    private String title;
    private String preis;
    private String laufleistung;
    private String erstzulassung;
    private String motorleistung;
    private LocalDate insertDate;
    private LocalDate receivedDate;

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

    @PrePersist
    public void prePersist() {
        if (this.insertDate == null) {
            this.insertDate = LocalDate.now();
        }
    }

    public BaseEntry() {
    }

    public BaseEntry(LocalDate insertDate, String link, String title, String preis, String laufleistung, String erstzulassung, String motorleistung) {
        this.insertDate = insertDate;
        this.link = link;
        this.title = title;
        this.preis = preis;
        this.laufleistung = laufleistung;
        this.erstzulassung = erstzulassung;
        this.motorleistung = motorleistung;
    }

    public BaseEntry(AutoScoutEntryDTO dto) {
        BeanUtils.copyProperties(dto, this);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
