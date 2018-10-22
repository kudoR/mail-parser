package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.HashMap;
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
        Function<ModelDetailPrice, BigDecimal> mapModelDetailToPrice = modelDetailPrice -> modelDetailPrice.getPreis();
        Function<ModelDetailPrice, LocalDate> mapModelDetailToLocalDate = modelDetailPrice -> modelDetailPrice.getErstzulassung();

        Function<Long, Double> mapLongToDouble = aLong -> aLong.doubleValue();
        Function<BigDecimal, Double> mapBigDecimalToDouble = bigDecimal -> bigDecimal.doubleValue();
        Function<LocalDate, Double> mapLocalDateToDouble = localDate -> functionMapLocalDateToDouble(localDate);

        HashMap<Integer, Double> laufleistungPercentiles = modelDetailPriceService.getPercentilesByModel(byModel, mapModelDetailToLaufleistung, mapLongToDouble);
        HashMap<Integer, Double> preisPercentiles = modelDetailPriceService.getPercentilesByModel(byModel, mapModelDetailToPrice, mapBigDecimalToDouble);
        HashMap<Integer, Double> ezPercentiles = modelDetailPriceService.getPercentilesByModel(byModel, mapModelDetailToLocalDate, mapLocalDateToDouble);

        HashMap<Integer, LocalDateTime> ezPercentilesFormatted = postProcessPercentiles(ezPercentiles);

        HashMap<String, HashMap> result = new HashMap<>();

        result.put("pricePercentiles", preisPercentiles);
        result.put("laufleistungPercentiles", laufleistungPercentiles);
        result.put("erstzulassungPercentiles", ezPercentilesFormatted);

        return result;
    }

    private HashMap<Integer, LocalDateTime> postProcessPercentiles(HashMap<Integer, Double> ezPercentiles) {
        HashMap<Integer, LocalDateTime> result = new HashMap<>();
        ezPercentiles.forEach((integer, aDouble) -> result.put(integer, LocalDateTime.ofInstant(Instant.ofEpochMilli(aDouble.longValue()), ZoneId.systemDefault())));
        return result;
    }

    private Double functionMapLocalDateToDouble(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        long millis = Timestamp.valueOf(localDateTime).getTime();
        return new Double(millis);
    }
}
