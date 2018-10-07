package de.jgh.pricetrend.mailparser;

import org.jsoup.nodes.Document;

import javax.persistence.*;

@Entity
public class DetailEntry {
    @EmbeddedId
    private DetailEntryId id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String documentHtml;
    private boolean processed;

    public DetailEntry() {
    }

    public DetailEntry(DetailEntryId id, String documentHtml) {
        this.id = id;
        this.documentHtml = documentHtml;
    }
}
