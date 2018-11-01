package de.jgh.pricetrend.mailparser;

import de.jgh.pricetrend.mailparser.repo.DetailEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ReportController {

    @Autowired
    private PercentileCalcService percentileCalcService;

    @Autowired
    private DetailEntryRepository detailEntryRepository;

    @GetMapping("/report/{model}/{mileage}/{registration}/{price}")
    public Object getReportForModelAndValues(
            @PathVariable(name = "model") String model,
            @PathVariable(name = "mileage") String mileage,
            @PathVariable(name = "registration") String registration,
            @PathVariable(name = "price") String price
    ) {
        HashMap<String, String> report = new HashMap<>();
        CalculatedPercentile mileagePercentile = percentileCalcService.mileagePercentile(model, mileage);
        CalculatedPercentile pricePercentile = percentileCalcService.pricePercentile(model, price);
        CalculatedPercentile registrationPercentile = percentileCalcService.registrationPercentile(model, registration);

        report.put("model", model);
        report.put("mileagePercentile", mileagePercentile.getLabel().name());
        report.put("pricePercentile", pricePercentile.getLabel().name());
        report.put("registrationPercentile", registrationPercentile.getLabel().name());

        report.put("minPrice", detailEntryRepository.findTopByModelOrderByPriceAsc(model).getPrice().toString());
        report.put("maxPrice", detailEntryRepository.findTopByModelOrderByPriceDesc(model).getPrice().toString());
        report.put("avgPrice", detailEntryRepository.getAveragePrice(model).toString());
        return report;
    }
}
