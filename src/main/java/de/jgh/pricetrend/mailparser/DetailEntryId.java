package de.jgh.pricetrend.mailparser;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;


@Embeddable
public class DetailEntryId implements Serializable {
    private String inseratId;
    private LocalDateTime dateTime;

    public DetailEntryId() {
    }

    public DetailEntryId(String inseratId) {
        this.inseratId = inseratId;
        this.dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
