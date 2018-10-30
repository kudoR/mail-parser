package de.jgh.pricetrend.mailparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ReportController {

    @Autowired
    private PercentileCalcService percentileCalcService;

    @GetMapping("/report/{model}")
    public Object getReportForModelAndValues(@PathVariable(name = "model") String model, String mileage, String price, String registration) {
        HashMap<String, String> report = new HashMap<>();
        CalculatedPercentile mileagePercentile = percentileCalcService.mileagePercentile(model, mileage);
        CalculatedPercentile pricePercentile = percentileCalcService.pricePercentile(model, price);
        CalculatedPercentile registrationPercentile = percentileCalcService.registrationPercentile(model, registration);

        report.put("model", model);
        report.put("mileagePercentile", mileagePercentile.getLabel().name());
        report.put("pricePercentile", pricePercentile.getLabel().name());
        report.put("registrationPercentile", registrationPercentile.getLabel().name());
        return report;
    }
}
