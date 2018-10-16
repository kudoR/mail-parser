package de.jgh.pricetrend.mailparser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryAvgPriceRepository extends JpaRepository<HistoryAvgPrice, Long> {
}
