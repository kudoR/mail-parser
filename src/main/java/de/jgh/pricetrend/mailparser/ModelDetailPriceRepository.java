package de.jgh.pricetrend.mailparser;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ModelDetailPriceRepository extends CrudRepository<ModelDetailPrice, Long> {
    List<ModelDetailPrice> findByModel(String model);
}
