package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.DetailEntry;
import de.jgh.pricetrend.mailparser.model.DetailEntryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailEntryRepository extends JpaRepository<DetailEntry, DetailEntryId> {
    List<DetailEntry> findByProcessed(boolean processed);
    DetailEntry findTopByProcessed(boolean processed);
    List<DetailEntry> findByIdInseratId(String inseratId);
}
