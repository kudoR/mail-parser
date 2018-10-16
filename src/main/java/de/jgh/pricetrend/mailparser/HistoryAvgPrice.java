package de.jgh.pricetrend.mailparser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class HistoryAvgPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal price;
    private LocalDate date;

    public HistoryAvgPrice(BigDecimal price) {
        this.price = price;
    }
}
