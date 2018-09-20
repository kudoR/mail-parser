package de.jgh.pricetrend.mailparser;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class ProcessedEntry implements Serializable {

    @Id
    private Long id;
    private String title;
    private BigDecimal preis;
    private Long laufleistung;
    private LocalDate erstzulassung;
    private Integer motorleistung;

    public ProcessedEntry() {
    }

    public ProcessedEntry(BaseEntry baseEntry) {
        this.id = baseEntry.getId();
        this.title = baseEntry.getTitle();
        this.preis = parsePreis(baseEntry.getPreis());
        this.laufleistung = parseLaufleistung(baseEntry.getLaufleistung());
        this.erstzulassung = parseErstzulassung(baseEntry);
        this.motorleistung = parseMotorleistung(baseEntry);
    }

    private Long parseLaufleistung(String toParse) {
        try {
            return new Long(toParse
                    .replace("km", "")
                    .replace(",", "")
                    .replace(".", "")
            );
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate parseErstzulassung(BaseEntry baseEntry) {
        String erstzulassung = baseEntry.getErstzulassung();
        if (erstzulassung.length() < 7) return null;
        Integer year = Integer.valueOf(erstzulassung.substring(3, 7));
        Integer month = Integer.valueOf(erstzulassung.substring(0, 2));
        return LocalDate.of(year, month, 1);
    }

    private Integer parseMotorleistung(BaseEntry baseEntry) {
        try {
            return Integer.parseInt(baseEntry
                    .getMotorleistung()
                    .replace("kW", "")
                    .trim()
            );
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parsePreis(String toParse) {
        try {
            return new BigDecimal(toParse
                    .replace("€", "")
                    .replace(",", "")
                    .replace("-", "")
                    .replace(".", "")
                    .trim()
            );
        } catch (Exception e) {
            return null;
        }
    }


    public ProcessedEntry(Long id, String title, BigDecimal preis, Long laufleistung, LocalDate erstzulassung, Integer motorleistung) {
        this.id = id;
        this.title = title;
        this.preis = preis;
        this.laufleistung = laufleistung;
        this.erstzulassung = erstzulassung;
        this.motorleistung = motorleistung;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public Long getLaufleistung() {
        return laufleistung;
    }

    public void setLaufleistung(Long laufleistung) {
        this.laufleistung = laufleistung;
    }

    public LocalDate getErstzulassung() {
        return erstzulassung;
    }

    public void setErstzulassung(LocalDate erstzulassung) {
        this.erstzulassung = erstzulassung;
    }

    public Integer getMotorleistung() {
        return motorleistung;
    }

    public void setMotorleistung(Integer motorleistung) {
        this.motorleistung = motorleistung;
    }
}
