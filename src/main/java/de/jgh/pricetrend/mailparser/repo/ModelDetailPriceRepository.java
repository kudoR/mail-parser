package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.ModelDetailPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelDetailPriceRepository extends JpaRepository<ModelDetailPrice, Long> {
    List<ModelDetailPrice> findByModel(String model);
    List<ModelDetailPrice> findTop1000ByOrderByIdDesc();
}
