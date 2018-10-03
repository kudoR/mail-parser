package de.jgh.pricetrend.mailparser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RawEntryRepository extends JpaRepository<RawEntry, Long> {
}
