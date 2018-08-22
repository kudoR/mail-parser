package de.jgh.pricetrend.mailparser;

public class AutoScoutEntryDTO {
    private String title;
    private String link;
    private String preis;
    private String laufleistung;
    private String erstzulassung;
    private String motorleistungInKw;

    public AutoScoutEntryDTO(String title, String link, String preis, String laufleistung, String erstzulassung, String motorleistungInKw) {
        this.title = title;
        this.link = link;
        this.preis = preis;
        this.laufleistung = laufleistung;
        this.erstzulassung = erstzulassung;
        this.motorleistungInKw = motorleistungInKw;
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

    public String getMotorleistungInKw() {
        return motorleistungInKw;
    }

    public void setMotorleistungInKw(String motorleistungInKw) {
        this.motorleistungInKw = motorleistungInKw;
    }
}
