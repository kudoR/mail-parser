package de.jgh.pricetrend.mailparser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailEntryRepository extends JpaRepository<DetailEntry, DetailEntryId> {
    List<DetailEntry> findByProcessed(boolean processed);
    DetailEntry findTopByProcessed(boolean processed);
}
