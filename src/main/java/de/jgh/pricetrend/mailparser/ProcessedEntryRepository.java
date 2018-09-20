package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.ProcessedEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEntryRepository extends JpaRepository<ProcessedEntry, Long> {
}
