package de.jgh.pricetrend.mailparser;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

@Service
public class ModelDetailPriceService {
    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;

    public HashMap<Integer, Double> getLaufleistungPercentilesByModel(List<ModelDetailPrice> byModel, Function function1, Function function2) {
        //  List<ModelDetailPrice> byModel = modelDetailPriceRepository.findByModel(model);
        // Function<ModelDetailPrice, Long> mapModelDetailToLaufleistung = modelDetailPrice -> modelDetailPrice.getLaufleistung();
        //  Function<ModelDetailPrice, BigDecimal> mapModelDetailToPreis = modelDetailPrice -> modelDetailPrice.getPreis();
        //Function function2 = aLong -> aLong.doubleValue();

        Double[] doubles = (Double[]) byModel
                .stream()
                .map(function1)
                .map(function2)
                .toArray(Double[]::new);

        Percentile percentile = new Percentile();
        HashMap<Integer, Double> laufleistungPercentiles = new HashMap<>();

        Arrays.asList(30, 50, 75, 90)
                .stream()
                .forEach(percentileValue -> {
                    laufleistungPercentiles.put(percentileValue,
                            percentile.evaluate(toPrimitive(doubles), percentileValue));
                });

        return laufleistungPercentiles;
    }
}
