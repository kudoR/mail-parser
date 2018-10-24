package de.jgh.pricetrend.mailparser;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

@Service
public class ModelDetailPriceService {

    public Set<CalculatedPercentile> getPercentilesByModel(
            List<ModelDetailPrice> byModel,
            Function function1,
            Function function2,
            PercentileType percentileType) {

        Double[] doubles = (Double[]) byModel
                .stream()
                .map(function1)
                .filter(o -> o != null)
                .map(function2)
                .toArray(Double[]::new);

        Percentile percentile = new Percentile();
        HashSet<CalculatedPercentile> calculatedPercentiles = new HashSet<>();

        Arrays.asList(PercentileEnum.values())
                .stream()
                .filter(percentileEnum -> percentileEnum.getPercentile() >= 0)
                .forEach(percentileEnum -> {
                    double value = percentile.evaluate(toPrimitive(doubles), percentileEnum.getPercentile());
                    calculatedPercentiles.add(new CalculatedPercentile(percentileType, value, percentileEnum));
                });

        return calculatedPercentiles;
    }

}
