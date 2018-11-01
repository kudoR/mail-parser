package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.model.ModelDetailPrice;
import de.jgh.pricetrend.mailparser.repo.ModelDetailPriceRepository;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.jgh.pricetrend.mailparser.PercentileType.*;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

@Service
public class PercentileCalcService {

    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;


    public CalculatedPercentile mileagePercentile(String mileage, List<ModelDetailPrice> byModel) {
        return getLaufleistungPercentilesForModel(byModel)
                .stream()
                .filter(calculatedPercentile -> calculatedPercentile.getDoubleValue() > Double.valueOf(mileage))
                .min(Comparator.comparingDouble(value -> value.getDoubleValue()))
                .orElse(new CalculatedPercentile(LAUFLEISTUNG, -1, PercentileEnum.MAX));
    }

    public CalculatedPercentile registrationPercentile(String registration, List<ModelDetailPrice> byModel) {
        return getRegistrationPercentilesForModel(byModel)
                .stream()
                .filter(calculatedPercentile -> calculatedPercentile.getLocalDateValue().compareTo(LocalDateTime.parse(registration)) > 1)
                .min(Comparator.comparing(value -> value.getLocalDateValue()))
                .orElse(new CalculatedPercentile(ERSTZULASSUNG, -1, PercentileEnum.MAX));
    }

    public CalculatedPercentile pricePercentile(String price, List<ModelDetailPrice> byModel) {
        return getPricePercentilesForModel(byModel)
                .stream()
                .filter(calculatedPercentile -> calculatedPercentile.getDoubleValue() > Double.valueOf(price))
                .min(Comparator.comparingDouble(value -> value.getDoubleValue()))
                .orElse(new CalculatedPercentile(PREIS, -1, PercentileEnum.MAX));
    }

    public Set<CalculatedPercentile> getLaufleistungPercentilesForModel(List<ModelDetailPrice> byModel) {
        return getPercentilesByModel(
                byModel,
                (Function<ModelDetailPrice, Long>) modelDetailPrice -> modelDetailPrice.getLaufleistung(),
                (Function<Long, Double>) aLong -> aLong.doubleValue(),
                LAUFLEISTUNG
        );
    }

    public Set<CalculatedPercentile> getRegistrationPercentilesForModel(List<ModelDetailPrice> byModel) {
        return postProcessPercentiles(getPercentilesByModel(
                byModel,
                (Function<ModelDetailPrice, LocalDate>) modelDetailPrice -> modelDetailPrice.getErstzulassung(),
                (Function<LocalDate, Double>) localDate -> functionMapLocalDateToDouble(localDate),
                ERSTZULASSUNG
        ));
    }

    public Set<CalculatedPercentile> getPricePercentilesForModel(List<ModelDetailPrice> byModel) {
        return getPercentilesByModel(
                byModel,
                (Function<ModelDetailPrice, BigDecimal>) modelDetailPrice -> modelDetailPrice.getPreis(),
                (Function<BigDecimal, Double>) bigDecimal -> bigDecimal.doubleValue(),
                PREIS
        );
    }

    private Set<CalculatedPercentile> postProcessPercentiles(Set<CalculatedPercentile> percentiles) {
        return percentiles
                .stream()
                .map(calculatedPercentile -> new CalculatedPercentile(
                                calculatedPercentile.getPercentileType(),
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(calculatedPercentile.getDoubleValue().longValue()), ZoneId.systemDefault()),
                                calculatedPercentile.getLabel()
                        )
                )
                .collect(Collectors.toSet());
    }

    private Set<CalculatedPercentile> getPercentilesByModel(
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

    private Double functionMapLocalDateToDouble(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        long millis = Timestamp.valueOf(localDateTime).getTime();
        return new Double(millis);
    }


//    public HashMap<String, Double> evaluation(String model,
//                                              String mileage,
//                                              String registration,
//                                              String price) {
//        CalculatedPercentile mileagePercentile = mileagePercentile(model, mileage);
//        CalculatedPercentile registrationPercentile = registrationPercentile(model, registration);
//        CalculatedPercentile pricePercentile = pricePercentile(model, price);
//
//        int mileagePercentileValue = mileagePercentile.getLabel().getPercentile();
//        int registrationPercentileValue = registrationPercentile.getLabel().getPercentile();
//
//        double evaluation = 0.75 * mileagePercentileValue + 0.25 * registrationPercentileValue;
//        HashMap<String, Double> evalResult = new HashMap<>();
//        evalResult.put("evaluatedNiveau", evaluation);
//
//
//        int givenPrice = pricePercentile.getLabel().getPercentile();
//        evalResult.put("priceNiveau", new Double(givenPrice));
//
//
//        return evalResult;
//    }
}
