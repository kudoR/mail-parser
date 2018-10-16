package de.jgh.pricetrend.mailparser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProcessedEntryRepository extends JpaRepository<ProcessedEntry, Long> {
    @Query(value = "select avg(preis) from processed_entry", nativeQuery = true)
    BigDecimal getActualAvgPrice();
}
