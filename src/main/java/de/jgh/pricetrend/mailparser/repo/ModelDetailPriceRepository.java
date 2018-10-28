package de.jgh.pricetrend.mailparser.repo;

import de.jgh.pricetrend.mailparser.model.ModelDetailPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ModelDetailPriceRepository extends CrudRepository<ModelDetailPrice, Long> {
    List<ModelDetailPrice> findByModel(String model);
    List<ModelDetailPrice> findTop1000ByOrderByIdDesc();
}
