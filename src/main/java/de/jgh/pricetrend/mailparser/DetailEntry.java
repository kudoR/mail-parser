package de.jgh.pricetrend.mailparser;

import org.jsoup.nodes.Document;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class DetailEntry {
    @EmbeddedId
    private DetailEntryId id;
    private String documentHtml;
    private boolean processed;

    public DetailEntry() {
    }

    public DetailEntry(DetailEntryId id, String documentHtml) {
        this.id = id;
        this.documentHtml = documentHtml;
    }
}
