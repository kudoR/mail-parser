package de.jgh.pricetrend.mailparser;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class BaseEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String preis;
    private String laufleistung;
    private String erstzulassung;
    private String motorleistung;

    public BaseEntry() {
    }

    public BaseEntry(String title, String preis, String laufleistung, String erstzulassung, String motorleistung) {
        this.title = title;
        this.preis = preis;
        this.laufleistung = laufleistung;
        this.erstzulassung = erstzulassung;
        this.motorleistung = motorleistung;
    }

    public BaseEntry(AutoScoutEntryDTO dto) {
        BeanUtils.copyProperties(dto, this);
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
