package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.ModelEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelEntryRepository extends JpaRepository<ModelEntry, Long> {

}
