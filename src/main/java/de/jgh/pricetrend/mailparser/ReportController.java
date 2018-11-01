package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.model.ModelDetailPrice;
import de.jgh.pricetrend.mailparser.repo.DetailEntryRepository;
import de.jgh.pricetrend.mailparser.repo.ModelDetailPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
public class ReportController {

    @Autowired
    private PercentileCalcService percentileCalcService;

    @Autowired
    private DetailEntryRepository detailEntryRepository;

    @Autowired
    private ModelDetailPriceRepository modelDetailPriceRepository;

    @GetMapping("/report/{model}/{mileage}/{registration}/{price}")
    public Object getReportForModelAndValues(
            @PathVariable(name = "model") String model,
            @PathVariable(name = "mileage") String mileage,
            @PathVariable(name = "registration") String registration,
            @PathVariable(name = "price") String price
    ) {
        HashMap<String, Object> report = new HashMap<>();
        List<ModelDetailPrice> byModel = modelDetailPriceRepository.findByModel(model);
        CalculatedPercentile actMileagePercentile = percentileCalcService.mileagePercentile(mileage, byModel);
        CalculatedPercentile actPricePercentile = percentileCalcService.pricePercentile(price, byModel);
        CalculatedPercentile actRegistrationPercentile = percentileCalcService.registrationPercentile(registration, byModel);

        Set<CalculatedPercentile> allLaufleistungPercentilesForModel = percentileCalcService.getLaufleistungPercentilesForModel(byModel);
        Set<CalculatedPercentile> allRegistrationPercentilesForModel = percentileCalcService.getRegistrationPercentilesForModel(byModel);
        Set<CalculatedPercentile> allPricePercentilesForModel = percentileCalcService.getPricePercentilesForModel(byModel);

        report.put("model", model);
        report.put("mileagePercentile", actMileagePercentile.getLabel().name());
        report.put("pricePercentile", actPricePercentile.getLabel().name());
        report.put("registrationPercentile", actRegistrationPercentile.getLabel().name());

        report.put("minPrice", detailEntryRepository.findTopByModelOrderByPriceAsc(model).getPrice().toString());
        report.put("maxPrice", detailEntryRepository.findTopByModelOrderByPriceDesc(model).getPrice().toString());
        report.put("avgPrice", detailEntryRepository.getAveragePrice(model).toString());

        report.put("allLaufleistungPercentilesForModel", allLaufleistungPercentilesForModel);
        report.put("allRegistrationPercentilesForModel", allRegistrationPercentilesForModel);
        report.put("allPricePercentilesForModel", allPricePercentilesForModel);
        return report;
    }
}
