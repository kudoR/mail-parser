package de.jgh.pricetrend.mailparser;

import javax.persistence.*;

@Entity
public class DetailEntry {
    @EmbeddedId
    private DetailEntryId id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String documentHtml;
    private Double price;
    private boolean processed;

    public DetailEntry() {
    }

    public DetailEntry(DetailEntryId id, String documentHtml) {
        this.id = id;
        this.documentHtml = documentHtml;
    }

    public String getDocumentHtml() {
        return documentHtml;
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
}
