package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
public class ModelDetailPriceController {
    @Autowired
    private ModelDetailPriceService modelDetailPriceService;

    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;

    @GetMapping("/percentiles/{model}")
    public Object getPercentilesForModel(@PathVariable("model") String model) {
        List<ModelDetailPrice> byModel = modelDetailPriceRepository.findByModel(model);

        Function<ModelDetailPrice, Long> mapModelDetailToLaufleistung = modelDetailPrice -> modelDetailPrice.getLaufleistung();
        Function<Long, Double> mapLongToDouble = aLong -> aLong.doubleValue();

        return modelDetailPriceService.getLaufleistungPercentilesByModel(byModel, mapModelDetailToLaufleistung, mapLongToDouble);
    }
}
