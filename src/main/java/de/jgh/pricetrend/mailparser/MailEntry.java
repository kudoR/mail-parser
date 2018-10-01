package de.jgh.pricetrend.mailparser;

import java.util.Date;

public class MailEntry {
    Date receivedDate;
    String content;

    public MailEntry(Date receivedDate, String content) {
        this.receivedDate = receivedDate;
        this.content = content;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public String getContent() {
        return content;
    }
}
