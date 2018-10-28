package de.jgh.pricetrend.mailparser.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Mail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Date receivedDate;
    private String sender;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;
    private boolean processed;

    public Mail() {
    }

    public Mail(String title, Date receivedDate, String sender, String content) {
        this.title = title;
        this.receivedDate = receivedDate;
        this.sender = sender;
        this.content = content;
    }

    public boolean isProcessed() {
        return processed;
    }

    public Mail setProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
