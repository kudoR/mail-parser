package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.jgh.pricetrend.mailparser.PercentileType.ERSTZULASSUNG;
import static de.jgh.pricetrend.mailparser.PercentileType.LAUFLEISTUNG;
import static de.jgh.pricetrend.mailparser.PercentileType.PREIS;

@RestController
public class ModelDetailPriceController {

    @Autowired
    private ModelDetailPriceService modelDetailPriceService;

    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;

    @GetMapping("/evaluation/{model}/{mileage}/{registration}/{price}")
    public HashMap<String, Double> evaluation(@PathVariable("model") String model,
                             @PathVariable("mileage") String mileage,
                             @PathVariable("registration") String registration,
                             @PathVariable("price") String price) {
        CalculatedPercentile mileagePercentile = mileagePercentile(model, mileage);
        CalculatedPercentile registrationPercentile = registrationPercentile(model, registration);
        CalculatedPercentile pricePercentile = pricePercentile(model, price);

        int mileagePercentileValue = mileagePercentile.getLabel().getPercentile();
        int registrationPercentileValue = registrationPercentile.getLabel().getPercentile();

        double evaluation = 0.75 * mileagePercentileValue + 0.25 * registrationPercentileValue;
        HashMap<String, Double> evalResult = new HashMap<>();
        evalResult.put("evaluatedNiveau", evaluation);


        int givenPrice = pricePercentile.getLabel().getPercentile();
        evalResult.put("priceNiveau", new Double(givenPrice));


        return evalResult;
    }

    @GetMapping("/percentile/mileage/{model}/{mileage}")
    public CalculatedPercentile mileagePercentile(@PathVariable("model") String model, @PathVariable("mileage") String mileage) {
        List<Set<CalculatedPercentile>> percentilesForModel = getPercentilesForModel(model);
        Set<CalculatedPercentile> mileagePercentiles = percentilesForModel.get(0);
        return mileagePercentiles
                .stream()
                .filter(calculatedPercentile -> calculatedPercentile.getDoubleValue() > Double.valueOf(mileage))
                .min(Comparator.comparingDouble(value -> value.getDoubleValue()))
                .orElse(new CalculatedPercentile(LAUFLEISTUNG, -1, PercentileEnum.MAX));
    }

    @GetMapping("/percentile/registration/{model}/{registration}")
    public CalculatedPercentile registrationPercentile(@PathVariable("model") String model, @PathVariable("registration") String registration) {
        List<Set<CalculatedPercentile>> percentilesForModel = getPercentilesForModel(model);
        Set<CalculatedPercentile> registrationPercentiles = percentilesForModel.get(1);
        return registrationPercentiles
                .stream()
                .filter(calculatedPercentile -> calculatedPercentile.getLocalDateValue().compareTo(LocalDateTime.parse(registration)) > 1)
                .min(Comparator.comparing(value -> value.getLocalDateValue()))
                .orElse(new CalculatedPercentile(ERSTZULASSUNG, -1, PercentileEnum.MAX));
    }

    @GetMapping("/percentile/price/{model}/{price}")
    public CalculatedPercentile pricePercentile(@PathVariable("model") String model, @PathVariable("price") String price) {
        List<Set<CalculatedPercentile>> percentilesForModel = getPercentilesForModel(model);
        Set<CalculatedPercentile> pricePercentiles = percentilesForModel.get(2);
        return pricePercentiles
                .stream()
                .filter(calculatedPercentile -> calculatedPercentile.getDoubleValue() > Double.valueOf(price))
                .min(Comparator.comparingDouble(value -> value.getDoubleValue()))
                .orElse(new CalculatedPercentile(PREIS, -1, PercentileEnum.MAX));
    }

    @GetMapping("/percentiles/{model}")
    public List<Set<CalculatedPercentile>> getPercentilesForModel(@PathVariable("model") String model) {
        List<ModelDetailPrice> byModel = modelDetailPriceRepository.findByModel(model);

        Function<ModelDetailPrice, Long> mapModelDetailToLaufleistung = modelDetailPrice -> modelDetailPrice.getLaufleistung();
        Function<ModelDetailPrice, BigDecimal> mapModelDetailToPrice = modelDetailPrice -> modelDetailPrice.getPreis();
        Function<ModelDetailPrice, LocalDate> mapModelDetailToLocalDate = modelDetailPrice -> modelDetailPrice.getErstzulassung();

        Function<Long, Double> mapLongToDouble = aLong -> aLong.doubleValue();
        Function<BigDecimal, Double> mapBigDecimalToDouble = bigDecimal -> bigDecimal.doubleValue();
        Function<LocalDate, Double> mapLocalDateToDouble = localDate -> functionMapLocalDateToDouble(localDate);

        Set<CalculatedPercentile> laufleistungPercentiles = modelDetailPriceService.getPercentilesByModel(byModel, mapModelDetailToLaufleistung, mapLongToDouble, LAUFLEISTUNG);
        Set<CalculatedPercentile> ezPercentiles = modelDetailPriceService.getPercentilesByModel(byModel, mapModelDetailToLocalDate, mapLocalDateToDouble, ERSTZULASSUNG);
        Set<CalculatedPercentile> preisPercentiles = modelDetailPriceService.getPercentilesByModel(byModel, mapModelDetailToPrice, mapBigDecimalToDouble, PREIS);

        return Arrays.asList(laufleistungPercentiles, postProcessPercentiles(ezPercentiles), preisPercentiles);
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

    private Double functionMapLocalDateToDouble(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        long millis = Timestamp.valueOf(localDateTime).getTime();
        return new Double(millis);
    }
}
