package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.DetailEntry;
import de.jgh.pricetrend.mailparser.model.DetailEntryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailEntryRepository extends JpaRepository<DetailEntry, DetailEntryId> {
    List<DetailEntry> findByProcessed(boolean processed);
    DetailEntry findTopByProcessed(boolean processed);
    List<DetailEntry> findByIdInseratId(String inseratId);
    DetailEntry findTopByModelOrderByPriceDesc(String model);
    DetailEntry findTopByModelOrderByPriceAsc(String model);
    @Query(value = "select avg(price) from detail_entry where model=:model", nativeQuery = true)
    Double getAveragePrice(@Param("model") String model);
}
