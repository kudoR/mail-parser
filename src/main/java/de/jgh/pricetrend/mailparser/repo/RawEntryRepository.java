package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.RawEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawEntryRepository extends JpaRepository<RawEntry, Long> {
    List<RawEntry> findByLink(String link);
    List<RawEntry> findByOffline(boolean offline);
}
